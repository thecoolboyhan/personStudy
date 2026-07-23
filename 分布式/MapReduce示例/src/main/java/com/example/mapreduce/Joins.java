package com.example.mapreduce;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

/**
 * MapReduce 中的两种 Join 实现：Map 端 Join vs Reduce 端 Join。
 * 全部基于 JDK 25（含本版定稿的新特性）编写。
 *
 * 业务场景：订单表(大) JOIN 用户表(小) ON 用户ID，得到 enrichment 后的订单。
 *
 * 用到的 JDK 25 相关新特性：
 *   - record（JDK 16 定稿）            ：纯数据载体 OrderRec / UserRec / EnrichedOrder
 *   - sealed + record 模式 + switch 模式匹配（JDK 21 定稿）：对输入做类型安全分流
 *   - 虚拟线程 Executors.newVirtualThreadPerTaskExecutor()（JDK 21 定稿）：并行执行 map
 *   - ScopedValue（JEP 506，JDK 25 定稿）：把“广播到每个节点”的小表安全共享给所有 worker 虚拟线程
 *   - Stream Gatherers（JEP 485，JDK 24 定稿 / JDK 25 可用）：在 Reduce 端做“分组后笛卡尔积”的自定义流操作
 */
public class Joins {

    /* ===================== 数据模型 ===================== */

    /** 订单（大表的一行）。实现 JoinInput 以便统一处理。 */
    public record OrderRec(int orderId, int userId, String product, double amount) implements JoinInput {}

    /** 用户（小表的一行）。实现 JoinInput 以便统一处理。 */
    public record UserRec(int userId, String userName, String city) implements JoinInput {}

    /** 用密封接口统一两类输入，便于在 map/reduce 中用 switch 模式匹配安全分流（无需打字符串标签）。 */
    public sealed interface JoinInput permits OrderRec, UserRec {}

    /** join 后的输出：订单 + 用户名 + 城市。 */
    public record EnrichedOrder(int orderId, String product, double amount, String userName, String city) {}

    /* ===================== 1) Map 端 Join（广播 / Replicated / Broadcast Join） ===================== */

    /**
     * ScopedValue：替代 ThreadLocal 的现代化方案（JDK 25 定稿，JEP 506）。
     * 用来承载“小表（用户表）”——它被一次性加载进内存，并“广播”到每个计算节点，
     * 模拟 Hadoop 的 DistributedCache。ScopedValue 不可变、线程安全，且能被在其中 fork 出的
     * 虚拟线程自动继承，非常适合把只读上下文共享给大量并发任务。
     */
    private static final ScopedValue<Map<Integer, UserRec>> USER_CACHE = ScopedValue.newInstance();

    /**
     * Map 端 Join：只有 Map 阶段，没有 Shuffle、没有 Reduce。
     * 小表已在内存（USER_CACHE），每个订单直接在 map 里查内存完成 join，O(1) 命中。
     */
    public static List<EnrichedOrder> mapSideJoin(List<OrderRec> orders, Map<Integer, UserRec> users)
            throws InterruptedException {

        // Map 端 Join：每个订单在自己的虚拟线程里完成 join，全程只查内存（无 Shuffle、无网络）。
        // 每个任务把“广播到本节点”的小表绑定到 ScopedValue，enrich() 通过 USER_CACHE.get() 读取——
        // 用 ScopedValue（JDK 25 定稿，JEP 506）替代 ThreadLocal，既不可变又线程安全。
        // 注：JDK 25 中 ScopedValue 的绑定不会经普通 ExecutorService 自动继承，故这里在任务内显式
        //     重新绑定。若要“绑定一次、所有子任务自动继承”，需配合 StructuredTaskScope（JDK 25 仍为 preview）。
        var results = new ConcurrentLinkedQueue<EnrichedOrder>();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var futures = orders.stream()
                    .map(order -> executor.submit(() ->
                            ScopedValue.where(USER_CACHE, users).call(() -> enrich(order))))
                    .toList();
            for (var f : futures) {
                try {
                    var e = f.get();
                    if (e != null) results.add(e);       // 丢掉无匹配用户的订单
                } catch (ExecutionException e) {
                    throw new RuntimeException("任务失败", e.getCause());
                }
            }
        }
        return List.copyOf(results);
    }

    /** 在 map 任务内：从 ScopedValue 广播缓存里查用户，完成 join（完全在内存，无网络）。 */
    private static EnrichedOrder enrich(OrderRec order) {
        UserRec u = USER_CACHE.get().get(order.userId());   // 跨虚拟线程读取广播缓存
        if (u == null) return null;
        return new EnrichedOrder(order.orderId(), order.product(), order.amount(), u.userName(), u.city());
    }

    /* ===================== 2) Reduce 端 Join（Repartition Join） ===================== */

    /**
     * Map：给每条记录输出“连接键 = 用户ID”，框架的 shuffle 会把同用户ID的记录汇到一起。
     * value 直接是 JoinInput（已携带来源类型），无需额外打 "O"/"U" 字符串标签。
     */
    public static class ReduceSideJoinMapper implements MapReduce.Mapper<Long, JoinInput, Integer, JoinInput> {
        @Override
        public void map(Long ignored, JoinInput value, MapReduce.Context<Integer, JoinInput> context) {
            int joinKey = switch (value) {            // 模式匹配 switch（JDK 21 定稿）
                case OrderRec o -> o.userId();
                case UserRec u -> u.userId();
            };
            context.write(joinKey, value);
        }
    }

    /**
     * Reduce：同一用户ID的订单与用户记录已被 shuffle 聚到一起，这里做笛卡尔积 join。
     * 用 Stream Gatherers（JDK 24/25）把“先把同组记录收齐、再做交叉连接”写成一个流操作。
     */
    public static class ReduceSideJoinReducer
            implements MapReduce.Reducer<Integer, JoinInput, Integer, EnrichedOrder> {
        @Override
        public void reduce(Integer userId, Iterable<JoinInput> values,
                           MapReduce.Context<Integer, EnrichedOrder> context) {
            StreamSupport.stream(values.spliterator(), false)
                    .gather(JOIN_GATHERER)                       // 收集 -> 交叉连接 -> 发射 EnrichedOrder
                    .forEach(e -> context.write(e.orderId(), e));
        }
    }

    /**
     * 自定义 Gatherer：先把同组(同 key)的所有记录缓冲到 Accum，
     * 结束阶段再做“订单 × 用户”笛卡尔积（即 join 的核心配对逻辑）。
     */
    private static final Gatherer<JoinInput, Accum, EnrichedOrder> JOIN_GATHERER =
            Gatherer.<JoinInput, Accum, EnrichedOrder>of(
                    Accum::new,
                    Gatherer.Integrator.ofGreedy((var state, var input, var downstream) -> {
                        switch (input) {
                            case OrderRec o -> state.orders.add(o);
                            case UserRec u -> state.users.add(u);
                        }
                        return true;
                    }),
                    (var a, var b) -> {                 // 合并器：并行时合并两个 Accum
                        a.orders.addAll(b.orders);
                        a.users.addAll(b.users);
                        return a;
                    },
                    (var state, var downstream) -> {    // 收尾器：做“订单 × 用户”笛卡尔积
                        for (var o : state.orders)
                            for (var u : state.users)
                                downstream.push(new EnrichedOrder(
                                        o.orderId(), o.product(), o.amount(), u.userName(), u.city()));
                    }
            );

    /** Gatherer 的状态容器：分别收集订单与用户。 */
    private static final class Accum {
        final List<OrderRec> orders = new ArrayList<>();
        final List<UserRec> users = new ArrayList<>();
    }

}

package com.example.mapreduce;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

/**
 * 极简 MapReduce 框架（纯 JDK 25，使用虚拟线程、record、Stream 等现代特性）。
 *
 * 泛型说明：
 *   K1/V1 —— Map 阶段输入键值类型（通常 K1 为行偏移量，V1 为文本行）
 *   K2/V2 —— Map 输出 / Reduce 输入的中间键值类型
 *   K3/V3 —— Reduce 输出的最终键值类型
 */
public class MapReduce<K1, V1, K2, V2, K3, V3> {

    /**
     * 不可变的键值对。
     * JDK 16+ 的 record：编译器自动生成构造器、访问器 key()/value()、equals/hashCode/toString，
     * 比手写 class 更简洁，适合作为纯数据载体。
     */
    public record Pair<K, V>(K key, V value) {}

    /** Mapper 接口：处理一条输入记录。@FunctionalInterface 允许用 lambda 实现。 */
    @FunctionalInterface
    public interface Mapper<K1, V1, K2, V2> {
        void map(K1 key, V1 value, Context<K2, V2> context);
    }

    /** Reducer 接口：处理同一个 key 的一组 value。@FunctionalInterface 允许用 lambda 实现。 */
    @FunctionalInterface
    public interface Reducer<K2, V2, K3, V3> {
        void reduce(K2 key, Iterable<V2> values, Context<K3, V3> context);
    }

    /** 收集器：Map/Reduce 通过它输出 <key, value>。 */
    public static class Context<K, V> {
        // synchronizedList 保证虚拟线程并发写入时的安全
        private final List<Pair<K, V>> output = Collections.synchronizedList(new ArrayList<>());

        public void write(K key, V value) {
            output.add(new Pair<>(key, value));
        }

        public List<Pair<K, V>> getOutput() {
            return output;
        }
    }

    private final Mapper<K1, V1, K2, V2> mapper;
    private final Reducer<K2, V2, K3, V3> reducer;

    public MapReduce(Mapper<K1, V1, K2, V2> mapper, Reducer<K2, V2, K3, V3> reducer) {
        this.mapper = mapper;
        this.reducer = reducer;
    }

    /**
     * 提交一个 Job 并执行，返回最终结果列表。
     * splits：已经切分好的多个输入分片（模拟不同节点的输入）。
     */
    public List<Pair<K3, V3>> run(List<List<Pair<K1, V1>>> splits) throws InterruptedException {
        var mapped = parallelMap(splits);     // 1) 并行 Map 阶段
        var grouped = shuffleAndSort(mapped); // 2) Shuffle & Sort：按 key 分组并排序
        return parallelReduce(grouped);       // 3) 并行 Reduce 阶段
    }

    /**
     * 并行 Map 阶段：为每个分片提交一个任务。
     * JDK 21+ 虚拟线程（Project Loom）：newVirtualThreadPerTaskExecutor 为每个任务创建一个轻量级虚拟线程，
     * 由 JVM 调度到平台线程上，能轻松支撑百万级并发，避免了传统线程池的容量规划。
     * ExecutorService 是 AutoCloseable，try-with-resources 会在结束时等待所有任务完成。
     */
    private List<Pair<K2, V2>> parallelMap(List<List<Pair<K1, V1>>> splits) throws InterruptedException {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            // var + Stream：把每个分片映射为一个异步任务
            var futures = splits.stream()
                    .map(split -> executor.submit(() -> {
                        var ctx = new Context<K2, V2>();
                        for (var record : split) {
                            mapper.map(record.key(), record.value(), ctx);
                        }
                        return new ArrayList<>(ctx.getOutput());
                    }))
                    .toList();

            var result = new ArrayList<Pair<K2, V2>>();
            for (var f : futures) {
                result.addAll(unsafeGet(f));
            }
            return result;
        }
    }

    /**
     * Shuffle & Sort：把相同 key 的中间结果汇聚到一起。
     * JDK 8+ Stream 的 groupingBy 三步式：
     *   - 分类函数 Pair::key         ：按 key 分组（shuffle）
     *   - () -> new TreeMap<>()       ：下游 Map 用 TreeMap，保证 key 有序（sort）
     *   - mapping(Pair::value, toList)：把分组后的 value 收集成 List
     */
    private Map<K2, List<V2>> shuffleAndSort(List<Pair<K2, V2>> mapped) {
        return mapped.stream()
                .collect(Collectors.groupingBy(
                        Pair::key,
                        TreeMap::new,
                        Collectors.mapping(Pair::value, Collectors.toList())));
    }

    /** 并行 Reduce 阶段：同样使用虚拟线程执行器，每个 key 一组交给一个 reduce 调用。 */
    private List<Pair<K3, V3>> parallelReduce(Map<K2, List<V2>> grouped) throws InterruptedException {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var futures = grouped.entrySet().stream()
                    .map(entry -> executor.submit(() -> {
                        var ctx = new Context<K3, V3>();
                        reducer.reduce(entry.getKey(), entry.getValue(), ctx);
                        return new ArrayList<>(ctx.getOutput());
                    }))
                    .toList();

            var result = new ArrayList<Pair<K3, V3>>();
            for (var f : futures) {
                result.addAll(unsafeGet(f));
            }
            return result;
        }
    }

    /**
     * 安全获取 Future 结果：把受检异常（ExecutionException / InterruptedException）包装为运行时异常，
     * 避免在每个调用点都写 throws，保持外层代码简洁。
     */
    private static <T> T unsafeGet(Future<T> future) {
        try {
            return future.get();
        } catch (ExecutionException e) {
            throw new RuntimeException("任务执行失败", e.getCause());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("任务被中断", e);
        }
    }
}

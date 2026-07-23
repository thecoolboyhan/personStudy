package com.example.mapreduce;

import java.util.*;

/**
 * 运行两种 Join 并对比结果。
 */
public class JoinDemo {
    public static void main(String[] args) throws Exception {
        // 小表：用户表（能放进内存，适合做 Map 端广播 join）
        Map<Integer, Joins.UserRec> users = Map.of(
                1, new Joins.UserRec(1, "Alice", "Beijing"),
                2, new Joins.UserRec(2, "Bob",   "Shanghai"),
                3, new Joins.UserRec(3, "Cara",  "Shenzhen")
        );

        // 大表：订单表（实际中可能上亿条，这里用少量示例）
        var orders = List.of(
                new Joins.OrderRec(101, 1, "iPhone",   7999.0),
                new Joins.OrderRec(102, 2, "Book",      59.0),
                new Joins.OrderRec(103, 1, "Mouse",    199.0),
                new Joins.OrderRec(104, 4, "Keyboard", 299.0),   // 用户4 不在小表里 -> 两条路径都会丢掉
                new Joins.OrderRec(105, 3, "Monitor", 1599.0),
                new Joins.OrderRec(106, 2, "Cable",     19.0)
        );

        System.out.println("====== 1) Map 端 Join（广播小表，无 Shuffle） ======");
        var mapSide = Joins.mapSideJoin(orders, users);
        mapSide.forEach(System.out::println);

        System.out.println("\n====== 2) Reduce 端 Join（框架 Shuffle 汇聚后配对） ======");
        // 把订单 + 用户拍平成一个输入流，切成多个分片喂给框架
        var all = new ArrayList<Joins.JoinInput>();
        orders.forEach(all::add);
        users.values().forEach(all::add);

        // 切成 3 个分片（模拟分布式输入被切分）
        var splits = new ArrayList<List<MapReduce.Pair<Long, Joins.JoinInput>>>();
        for (int i = 0; i < all.size(); i += 2) {
            var split = new ArrayList<MapReduce.Pair<Long, Joins.JoinInput>>();
            for (int j = i; j < Math.min(i + 2, all.size()); j++)
                split.add(new MapReduce.Pair<>((long) j, all.get(j)));
            splits.add(split);
        }

        var job = new MapReduce<>(new Joins.ReduceSideJoinMapper(), new Joins.ReduceSideJoinReducer());
        var reduceSide = job.run(splits);
        reduceSide.forEach(p -> System.out.println(p.value()));
    }
}

package com.example.mapreduce;

import java.util.*;

/**
 * 入口：准备输入分片、提交 MapReduce Job、打印结果。
 */
public class Main {
    public static void main(String[] args) throws Exception {
        // 文本块（Text Blocks，JDK 15+ 稳定）：多行字符串更直观，适合表示一篇“文档”
        String document = """
                hello world hello mapreduce
                mapreduce is a distributed computing model
                hello distributed world
                mapreduce mapreduce mapreduce
                分布式 计算 mapreduce 分布式 系统
                world hello world
                """;

        // 把文档按行切成 3 个分片（split），模拟分布式输入被切分的过程
        var lines = document.lines().toList();           // JDK 16+ Stream.toList()：返回不可变列表
        var splits = new ArrayList<List<MapReduce.Pair<Long, String>>>();
        for (int i = 0; i < lines.size(); i += 2) {
            var split = new ArrayList<MapReduce.Pair<Long, String>>();
            for (int j = i; j < Math.min(i + 2, lines.size()); j++) {
                split.add(new MapReduce.Pair<>((long) j, lines.get(j)));
            }
            splits.add(split);
        }

        // 构造 Job 并提交运行（框架内部使用虚拟线程并行执行 Map/Reduce）
        var job = new MapReduce<>(new WordCount.WordCountMapper(), new WordCount.WordCountReducer());
        var result = job.run(splits);

        // record 的访问器为 key() / value()；forEach + lambda 打印结果
        System.out.println("====== WordCount 结果 ======");
        result.forEach(p -> System.out.println(p.key() + " : " + p.value()));
    }
}

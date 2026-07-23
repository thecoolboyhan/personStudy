package com.example.mapreduce;

import java.util.*;
import java.util.stream.*;

/**
 * 经典示例：单词计数 WordCount。
 *
 * 输入：若干文本行（key = 行号，value = 一行文本）
 * Map：把每行按空白拆成单词，输出 <单词, 1>
 * Reduce：把同一单词的 1 累加，输出 <单词, 出现总次数>
 */
public class WordCount {

    /** Map 阶段：把一行文本拆成单词并输出 <单词, 1>。 */
    public static class WordCountMapper implements MapReduce.Mapper<Long, String, String, Integer> {
        @Override
        public void map(Long ignoredLineNo, String value, MapReduce.Context<String, Integer> context) {
            // 转小写 -> 按空白拆分 -> 过滤掉标点等非单词字符 -> 丢弃空白 -> 写出
            // 全程使用 Stream，配合 var 与 lambda，风格更现代
            Arrays.stream(value.toLowerCase().split("\\s+"))
                  .map(w -> w.replaceAll("[^a-z0-9\\u4e00-\\u9fa5]", ""))
                  .filter(w -> !w.isBlank())                  // JDK 11+ String.isBlank()：判断是否为空或仅含空白
                  .forEach(w -> context.write(w, 1));
        }
    }

    /** Reduce 阶段：把同一单词的一组计数累加。 */
    public static class WordCountReducer implements MapReduce.Reducer<String, Integer, String, Integer> {
        @Override
        public void reduce(String key, Iterable<Integer> values, MapReduce.Context<String, Integer> context) {
            // StreamSupport 把 Iterable 转为 Stream，再用 mapToInt + sum 完成聚合
            int sum = StreamSupport.stream(values.spliterator(), false)
                                   .mapToInt(Integer::intValue)
                                   .sum();
            context.write(key, sum);
        }
    }
}

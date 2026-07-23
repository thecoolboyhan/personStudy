# MapReduce 示例

MapReduce 是 Google 提出的分布式计算模型，用于把大规模数据集的运算分拆成两个阶段：

- **Map（映射）**：把输入切分成若干分片，每个分片由一个 Map 任务并行处理，输出 `<key, value>` 中间结果。
- **Shuffle / Sort（洗牌排序）**：框架把相同 key 的中间结果汇聚到一起，按 key 排序后交给 Reduce。
- **Reduce（归约）**：对同一个 key 的一组 value 做汇总计算，输出最终结果。

优点：开发者只需写 `map` 和 `reduce` 两个函数，框架负责**任务调度、数据切分、容错、并行**等复杂细节。

下面给出一个**不依赖 Hadoop、可直接用 JDK 运行**的极简 MapReduce 框架实现（用虚拟线程模拟分布式并行），并以经典的 `WordCount` 单词计数作为示例。代码已改为 **JDK 25 风格**，使用了记录（record）、虚拟线程、Stream API、文本块、`var` 等新特性。

## 目录结构（Maven 工程）

```
MapReduce示例/
├── pom.xml                                       # Maven 工程，编译级别 release=25
└── src/main/java/com/example/mapreduce/
    ├── MapReduce.java     # 极简 MapReduce 框架（Mapper/Reducer 接口 + 并行执行引擎）
    ├── WordCount.java      # 单词计数示例 Job
    └── Main.java           # 入口：准备输入、提交任务、打印结果
```

## 用到的 JDK 25 新特性

- **虚拟线程（JDK 21+）**：`Executors.newVirtualThreadPerTaskExecutor()`，每个分片/key 一个轻量级虚拟线程，轻松支持高并发。
- **record（JDK 16+）**：`Pair<K,V>` 作为不可变键值对，自动生成访问器 `key()`/`value()`。
- **Stream API**：`groupingBy` 完成 shuffle & sort；`StreamSupport` + `mapToInt().sum()` 完成 Reduce 聚合。
- **文本块（JDK 15+）**：用 `"""..."""` 直观表示输入文档。
- **var（JDK 10+）**：局部变量类型推断，配合 lambda 让代码更简洁。
- **String.isBlank() / Stream.toList()** 等实用 API。

## 运行方式

```bash
# Maven（需 JDK 25）
cd MapReduce示例
mvn compile
mvn exec:java -Dexec.mainClass=com.example.mapreduce.Main
# 或打 jar：mvn package && java -jar target/mapreduce-demo.jar

# 仅用 JDK 手动编译运行
javac -d target/classes src/main/java/com/example/mapreduce/*.java
java -cp target/classes com.example.mapreduce.Main
```

## 核心执行流程

```
输入文件
  └─ split 切分（模拟多个分片）
       └─ 并行 Map   -> <单词, 1>
            └─ shuffle & sort（按 key 分组）
                 └─ 并行 Reduce -> <单词, 总次数>
```

> 说明：真实 Hadoop 中 Map/Reduce 运行在集群的不同节点上，本示例用多线程在同一进程内模拟“并行执行”，重点在于帮助理解 MapReduce 的编程模型与数据流。

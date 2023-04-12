# java8(2014)

## interface

为接口新加入了两种可以有方法体的写法。

- default:

  实现此接口的类，可以实现此方法，如果没有实现此方法，默认使用接口中的方法。

  > 如果一个类实现了多个接口，而且多个接口存在相同的默认方法，则此类必须重写接口的默认方法。

- static

  接口中允许写静态方法，调用时直接通过class文件调用。



## 函数式接口

@FunctionalInterface

有且只有一个抽象方法，但可以有多个非抽象方法的接口。



## Lambda表达式

一种语法糖写法，可以配合上面的函数式接口使用。



## Stream

> 通过流，把文件从一个地方输入到另一个地方，只负责内容的搬运，对文件内容不做任何CRUD。



## Optional

java对于空指针问题更优雅的处理方案。



## Date-Time API

- 过去java.util.Date类存在的问题

1. 非线程安全
2. 时区处理麻烦
3. 各种格式化、和时间计算繁琐。
4. 设计有缺陷，Date类通输出包含日期和时间。

# java9（2017.09）



## JShell

可以在命令窗口直接输入表达式并查看执行结果。



## 模块化系统

把jdk封装成不同的模块，然后自由组合。



## G1成为默认回收器

G1从jdk7被引入，从现在开始变成默认回收器。

## 快速创建不可变集合

增加了`List.of()`、`Set.of()`、`Map.of()` 和 `Map.ofEntries()`等工厂方法来创建不可变集合。

使用.of()创建的集合为不可变集合，不能增加，修改，排序，删除，不然会报异常。



## String存储结构优化

java8的string类是用char[]存储，在9之后改用byte[]数组存储字符串，节省了空间。



## 接口的私有方法

java9运行接口中存在私有方法。



## try-with-resources增强



## 进程API

增加了一套新的API来管理原生线程

``` java
// 获取当前正在运行的 JVM 的进程ProcessHandle currentProcess =ProcessHandle.current();// 输出进程的 idSystem.out.println(currentProcess.pid());// 输出进程的信息System.out.println(currentProcess.info());

///------

//著作权归所有 原文链接：https://javaguide.cn/java/new-features/java9.html
```



## 响应式流（重点）

TODO: 后面看明白了再来记这部分的笔记

[Java 9 揭秘（17. Reactive Streams） - 林本托 - 博客园 (cnblogs.com)](https://www.cnblogs.com/IcanFixIt/p/7245377.html)



# java10（2018.03）



## 局部变量类型推断（var）

可以直接使用var来声明局部变量

var关键字只能用在带有构造器的局部变量和for循环中。

var并不会改变java是一门静态语言的事实，编译器负责推断出类型。



## G1并行FULLGC

G1的FULLGC改成并行的标记清除算法，同时会使用和年轻代回收和混合回收相同的并行工作线程数，从而减少FULLGC的时间，以带来更好的性能和更大的吞吐量。



## 集合增强

`List`，`Set`，`Map` 提供了静态方法`copyOf()`返回入参集合的一个不可变拷贝。



# java11（2018.09）



## HTTP Client标准化

可以直接使用HttpClient来直接发送异步非阻塞的请求。



## String增强

对string增加了判空和统计的API



## ZGC（可伸缩低延迟垃圾回收器）

引入了ZGC，详细的可以看《读深入理解java虚拟机有感》.



# java12

## Shenandoah GC

引入了谢南多厄垃圾回收器。（谢南多厄垃圾回收器有java8版本sdk）



## G1优化

- 可终止收集：可以终止回收的过程。

- 及时返回未使用的已分配内存

  可以把空间的java堆返回给操作系统。



# java13



## 增强ZGC（释放未使用的内存）

之前的ZGC存在未能主动的把堆内存空间释放给操作系统的问题。

ZGC回收过程中的为每块内存分配的ZPageCache，现在使用LRU排序，并按照大小进行组织。

ZGC会向操作系统释放长时间未使用的页面。



## SocketAPI重构

对 SocketAPI进行了重写，使用了JUC包下的锁，而不是使用sync方法。



# java 14

## 空指针异常精准提示

可以精准的提示出空指针异常到底在哪个方法调用的时候报出的。



## 移除了CMS垃圾回收器



# java15

## ZGC转正

ZGC可以正式使用了，不过默认的回收器还是G1。

ZGC可以支持mac和windows了。



## 提出了密封类概念

一个类，不能随便被继承和修改。

## 禁用和废弃偏向锁

偏向锁的引入增加了JVM的复杂性大于其带来的性能提升。



# java16（2021.03）

## 启用C++14语言特性



## ZGC并发线程堆栈处理

java16将ZGC线程处理从安全点转移到一个并发阶段，可以极大的提高应用程序的性能和效率。



# java17（2021.09）

## 新的随机数生成方法

增加了新的接口类型和实现。可以让开发者更好的修改随机数。



## switch类型匹配

类似于switch的instanceof

## 删除RMI远程调用激活机制

删除了RMI远程调用的激活机制，但是保留了RMI的其余部分。

## 密封类（转正）

密封类正式可以使用。




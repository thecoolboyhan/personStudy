# 快速入门

## 基本概念



### 分组概念：

- appName：应用名称，建议和用户实际接入PowerJob 的应用名称保持一致，用于业务分组与隔离，一个appName等于一个业务集群，也就是实际的一个Java项目。

### 核心概念：

- 任务（Job）：描述了需要被PowerJob调度的任务信息，包括任务名称、调度时间、处理信息等。
- 任务实例（JobInstance，简称Instance）：任务被调度执行后会生成任务实例，任务实例记录了任务的运行时信息（任务与任务实例的关系类似于类与对象的关系）。
- 作业（Task）：任务实例的执行单元，一个JobInstance存在至少一个Task，具体规则如下：
  - 单机任务（STANDALONE）：一个JobInstance对应一个Task
  - 广播任务（BROADCAST）：一个JobInstance对应N个Task，N为集群机器数量，即每一台机器都会生成一个Task。
  - Map/MapReduce任务：一个JobInstance对应若干个Task，由开发者手动map产生。
- 工作流（Workflow）：由DAG（有向无环图）描述的一组任务（Job），用于任务编排。
- 工作流实例（WorkflowInstance）：工作流被调度执行后会生成工作流实例，记录了工作流的运行时信息。

### 扩展概念

- JVM容器：以Maven工作项目的维度组织一堆Java文件（开发者开发的众多Java处理器），可以通过前端网页动态发布并被执行器加载，具有极强的扩展能力和灵活性。
- OpenAPI：允许开发者通过接口来完成手工的操作，让系统整体变得更加灵活。开发者可以基于API便捷地扩展PowerJob原有的功能。
- 轻量级任务：单机执行且不需要以固定频率或者固定延迟执行的任务（>=v4.2.1)
- 重量级任务：非单机执行或者以固定频率/延迟执行的任务（>=v4.2.1)

### 定时任务类型

- API：该任务只会由powerjob-client中提供的OpenAPI接口触发，server不会主动调度。
- CRON：该任务的调度时间由CRON表达式指定。
- 固定频率：秒级任务，每隔多少毫秒运行一次，功能与java.util.concurrent.ScheduledExecutorService#scheduleAtFixedRate相同。
- 固定延迟：秒级任务，延迟多少毫秒运行一次，功能与java.util.concurrent.ScheduledExecutorService#scheduleWithFixedDelay相同。
- 工作流：该任务只会由其所属的工作流调度执行，server不会主动调度该任务。如果该任务不属于任何一个工作流，该任务就不会被调度。

>  备注：固定延迟任务和固定频率任务统称秒级任务，这两种任务无法被停止，只有任务被关闭或删除时才能真正停止任务。



### 项目结构说明：

``` 
├── LICENSE
├── powerjob-client // powerjob-client，普通Jar包，提供 OpenAPI
├── powerjob-common // 各组件的公共依赖，开发者无需感知
├── powerjob-remote // 内部通讯层框架，开发者无需感知
├── powerjob-server // powerjob-server，基于SpringBoot实现的调度服务器
├── powerjob-worker // powerjob-worker, 普通Jar包，接入powerjob-server的应用需要依赖该Jar包
├── powerjob-worker-agent // powerjob-agent，可执行Jar文件，可直接接入powerjob-server的代理应用
├── powerjob-worker-samples // 教程项目，包含了各种Java处理器的编写样例
├── powerjob-worker-spring-boot-starter // powerjob-worker 的 spring-boot-starter ，spring boot 应用可以通用引入该依赖一键接入 powerjob-server
├── powerjob-official-processors // 官方处理器，包含一系列常用的 Processor，依赖该 jar 包即可使用
├── others
└── pom.xml

```



# 正式

## 调度中心（Powerjob-server）

> 一个公司统一部署Powerjob-server集群，各业务线应用直接接入使用。



## 处理器（Processor）

- Java处理器可根据代码所处位置划分为内置Java处理器和外置Java处理器，前者直接集成在宿主应用（也就是接入本系统的业务应用）中，一般用来处理业务需求；后者可以在一个独立的轻量级的Java工程中开发，通过JVM容器技术被worker集群热加载，提供Java的“脚本能力”，一般用于处理灵活多变的需求。
- Java处理器可根据功能划分为单机处理器、广播处理器、Map处理器和MapReduce处理器。
  - 单机处理器（BasicProcessor）对应了单机任务，即某个任务的某次运行只会有某一台机器的某一个线程参与运算。
  - 广播处理器（BroadcastProcessor）对应了广播任务，即某个任务的某次运行会调用集群内所有机器参与运算。
  - Map处理器（MapProcessor）对应了Map任务，即某个任务在运行过程中，允许产生子任务并分发到其他机器进行运算。
  - MapReduce处理器（MapReduceProcessor）对应了MapReduce任务，在Map任务的基础上，增加了所有任务结束后的汇总统计。


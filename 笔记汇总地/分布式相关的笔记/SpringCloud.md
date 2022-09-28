# Spring Cloud

## 微服务的思想和优缺点

> 微服务架构设计的核心思想就是”微“，拆分的粒度相对比较小，这样的话单一职责，开发的耦合度就会降低，微小的功能可以独立部署扩展、灵活性强，升级改造影响范围小。



- 优点

1. 便于特定业务功能聚焦
2. 每个微服务都可以被一个小团队单独实施（开发，测试、部署上线、运维），团队合作一定程度解耦，便于实施敏捷开发。
3. 便于重用和模块之间的组装
4. 那么不同的微服务可以使用不同的语言开发，松耦合
5. 更容易引入新技术
6. 可以更好的实现DevOps开发运维一体化

- 缺点

1. 分布式复杂难以管理，当服务数量增加，管理将越加复杂
2. 分布式链路跟踪难等。

### 常用概念

- 服务注册与发现

服务注册：服务提供者将所提供服务的信息（服务器IP和接口，服务访问协议等）注册/登记到注册中心。

服务发现：服务消费者能够从注册中心获取到实时的服务列表，然后根究一定的策略选择一个服务访问。

- 负载均衡

负载均衡即将请求压力分配到多个服务器（应用服务器、数据库服务器等），以此来提高服务的性能、可靠性。

- 熔断

熔断即断路保护。微服务架构中，如果下游服务因访问压力过大而响应变慢或者失败，上游服务为了保护系统整体可用性，可以暂时对下游服务的调用这种牺牲局部，保全整体的措施就叫做熔断。

- 链路追踪

对一次请求涉及的很多个服务链路进行日志记录、性能监控。

- API网关

不同的微服务往往会有不同的访问地址，API请求先请求网关，通过网关路由到对应的服务。

网关的功能：

1. 统一接入（路由）
2. 安全防护（统一鉴权，网关访问的身份验证，与认证中心通信，）
3. 黑白名单（通过IP地址控制禁止访问网关功能）
4. 协议适配（实现通信协议校验，适配转换的功能）
5. 流量控制（限流）
6. 长短链接支持
7. 容错能力（负载均衡）



- Spring Cloud核心组件

![](https://www.helloimg.com/images/2022/09/16/Z6ighb.png)

工作流程

![](https://www.helloimg.com/images/2022/09/16/Z6iVWD.png)



- 使用RestTemplate调用远程服务时存在的问题

1. URL地址硬编码
2. 服务消费者要自己实现负载均衡
3. 消费者不清楚服务提供者的状态



# 第一代Spring Cloud核心组件

![](https://www.helloimg.com/images/2022/09/17/Z62Nx1.png)

## Eureka服务注册中心（目前已停止更新）

> 服务注册中心本质上是为了解耦服务提供者和服务消费者

- 服务注册中心一般原理

![](https://www.helloimg.com/images/2022/09/17/Z6TxNq.png)

1. 服务提供者启动
2. 服务提供者把相关服务主动注册到注册中心
3. 消费者获取服务注册信息：
   - poll模式：消费者主动拉取可用的服务提供者清单
   - push模式：消费者订阅服务（当订阅的服务有变化时，注册中心也会主动推送更新后的服务清单给消费者）
4. 消费者直接调用服务提供者。

注册中心也需要完成服务提供者的健康监控。

### 与主流注册中心的对比

- Zookeeper

> 存储+存储监听通知

ZK做服务注册中心。主要是因为它具有节点变更通知功能，客户端订阅节点，服务节点的所有变化，都能及时的通知到监听客户端。只要半数以上的选举剪短存活，整个集群就是可用的。

- Eureka

基于RestfulAPI风格开发的服务注册与发现组件。

- Consul

基于go开发的注册服务软件，采用Raft算法保证服务的一致性。（不在维护）

- Nacos

阿里巴巴，云原生注册中心和配置中心。



| 组件名    | 语言 | CAP                          | 对外暴露的接口 |
| --------- | ---- | ---------------------------- | -------------- |
| Eureka    | java | AP（自我保护机制，保证可用） | HTTP           |
| Consul    | Go   | CP（服务挂了，就立刻踢）     | HTTP/DNS       |
| Zookeeper | java | CP                           | 客户端         |
| Nacos     | java | 支持AP/CP切换                | HTTP           |

P：分区容错性（一定要满足的）

C：数据一致性

A：高可用

CAP不可能同时满足三个



Eureka主要有Server和Client

服务器启动之后会每30秒向Server发送心跳

如果Server90秒没有收到心跳，就会注销掉某个节点

Client端会缓存Server中的信息，即使所有Server宕机了，Client依然可以使用缓存中的数据去调用服务提供者。

> Eureka通过心跳检测、健康检查和客户端缓存等机制，提高系统的灵活性、可伸缩性和可用性。



### 角色工作

#### 客户端

> 提供者，要向EurekaServer注册服务。并完成服务续约等工作

- 服务提供者：

  1. 向注册中心注册服务
  2. Eureka注册中心保存服务的信息

- 服务续约（心跳检测）

  服务提供者每隔30秒向注册中心续约一次，如果没有续约，90秒后，注册中心会认为服务失效。

- 服务消费者：

  每隔30秒，从注册中心拉取一份服务列表，然后把服务信息缓存到本地

  

#### 服务端

- 服务下线

  服务正常关闭，客户端会注册去注册中心下线

  服务器利用定时任务，检测服务是否到期但未心跳，让他下线。

- 自我保护（宁可不杀，避免误杀）

  > 假设服务提供者和注册中心的网络出现了问题，不易代表着消费者和提供者之间也出现了问题，所以有了自我保护机制。

  当15分钟内注册中心中85%的节点都没有向注册中心发送心跳，服务器会认为是自己出了问题，服务器进入自我保护模式。

  1. 不会主动下线任何服务实例。
  2. 依然可以接收新的服务注册，当网络问题后，注册的节点依然可以同步给其他服务器。



### 源码

- server启动过程

利用了springboot 自动装配功能，

![](https://www.helloimg.com/images/2022/09/18/Z6QHK6.png)



![](https://www.helloimg.com/images/2022/09/18/Z6QlFn.png)

要装配该类，容器中必须有Marker这个Bean，才能状态Eurekaserver

启动时添加的@EnableEurekaServer注解，就是要注入上面所需的Marker类。

有了上面的Marker就可以注入EurekaServerAutoConfiguration类了

![](https://www.helloimg.com/images/2022/09/18/Z6Q2zz.png)

注入Eureka的仪表盘界面Controller。

![](https://www.helloimg.com/images/2022/09/18/Z6QOv0.png)

Eureka集群之间的实例是对等的，没有主从之分，所以启动过程中需要把自己注册到集群中。

注册过程中，要创建一个PeerEurekaNodes对象，此对象的start方法

![](https://www.helloimg.com/images/2022/09/18/Z6Qcfm.png)

会创建一个线程池





## Ribbon负载均衡

常见的客户端负载均衡工具。

- 与RestTemplate配合

```java
//使用RestTemplate模板对象调用远程
@Bean
@LoadBalanced   //ribbon负载均衡
public RestTemplate getRestTemplate(){
    return new RestTemplate();
}
```

> 只需要在获取RestTemplate的类上加@LoadBalanced即可，默认的负载均衡方式为轮训

### 负载均衡策略

| 负载均衡策略                                | 描述                                                         |
| ------------------------------------------- | ------------------------------------------------------------ |
| RoundRobinRule：轮询策略                    | 默认超过10次获取到的server都不可⽤，会返回 ⼀个空的server    |
| RandomRule：随机策略                        | 如果随机到的server为null或者不可⽤的话，会 while不停的循环选取 |
| RetryRule：重试策略                         | ⼀定时限内循环重试。默认继承 RoundRobinRule，也⽀持⾃定义注⼊， RetryRule会在每次选取之后，对选举的server进 ⾏判断，是否为null，是否alive，并且在500ms 内会不停的选取判断。⽽RoundRobinRule失效 的策略是超过10次，RandomRule是没有失效时 间的概念，只要serverList没都挂。 |
| BestAvailableRule：最⼩连接数策略           | 遍历serverList，选取出可⽤的且连接数最⼩的⼀个server。该算法⾥⾯有⼀个LoadBalancerStats的成员变量，会存储所有server的运⾏状况和连接数。如果选取到的server为null那么会调⽤ RoundRobinRule重新选取。1（1）2（1） 3（1） |
| AvailabilityFilteringRule： 可⽤过滤策略    | 扩展了轮询策略，会先通过默认的轮询选取⼀个 server，再去判断该server是否超时可⽤，当前 连接数是否超限，都成功再返回。 |
| ZoneAvoidanceRule：区域权衡策略（默认策略） | 扩展了轮询策略，继承了2个过滤器： ZoneAvoidancePredicate和 AvailabilityPredicate，除了过滤超时和链接数过多的server，还会过滤掉不符合要求的zone区域⾥⾯的所有节点，AWS --ZONE 在⼀个区域/机房 内的服务实例中轮询 |



- 修改负载策略

```yaml
#针对的被调⽤⽅微服务名称,不加就是全局⽣效
service-resume:
  ribbon:
    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule #负载策略调整
```

![](https://www.helloimg.com/images/2022/09/19/Z6kscu.png)

客户端再发送request请求之前，ribbon会生成拦截器，来利用负载策略，把请求负载到对应的服务器上。



### 源码

使用LoadBalanced注解后可以将普通的RestTemplate对象使用LoadBalancerClient来处理



借助springboot的自动装配

jar包下的spring.factories

```
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration
```

![](https://www.helloimg.com/images/2022/09/20/Z85OeM.png)

ribbon在启动时，会扫描所有添加了LoadBalanced注解的RestTemplate类，给每个类添加自己定制的拦截器。

然后开启一个定时任务，每隔一段时间就获取EurekaClient缓存获取新的服务实例信息，更新到Ribbon的本地缓存中。

![](https://www.helloimg.com/images/2022/09/20/Z85cTP.png)



## Hystrix（豪猪）熔断器

> 容错机制，让整个集群不会因为某一台机器或者服务处理速慢，而整个无法运行。

### 雪崩效应的解决方案

- 服务熔断

  当某个节点服务不可用或者响应时间太长，熔断该节点微服务的顿斯，进行服务的降级，快速返回响应信息。当检测到该节点微服务调用响应正常后，回复调用链路。

  > 服务熔断重点在“断”，切断对下游服务的调用。
  >
  > 熔断和降级一般一起使用。

  

- 服务降级

  整体资源不够用，先将一部分不重要的服务停掉，（调用我的时候返回一个预留值）等度过难关在打开服务。

  > 服务降级一般是从整体考虑的，就是当某个服务熔断后，服务器将不在被调用，但也比直接挂了强。

- 服务限流

  当服务器压力过大，但所有功能都很重要不能停掉，就可以利用限流来限制并发数。

  常见的限流措施：

  1. 限制总并发数（比如数据库连接池，线程池）
  2. 限制瞬时并发数（如Nginx限制瞬时并发数）
  3. 限制时间窗口我的平均速率（如Guava的RateLimiter、nginx的limit_req模块）
  4. 限制远程接口调用速率，加入MQ等。

  

### Hystrix

> Hystrix由Netfix开源的一个延迟和容错库，用于隔离访问远程系统。

- 包裹请求：使用HystrixCommand包裹对依赖的调用逻辑
- 跳闸机制：当某服务的错误率超过一定的阈值时，Hystrix可以跳闸，停止请求gd该服务一段时间
- 资源隔离：Hystrix为每个依赖都维护了一个小型的线程池（舱壁模式）（或者型号量）。如果该线程池已满，发往该依赖的请求就被立即拒绝，而不是排队等待，从而加速失败判定。
- 监控：Hystrix可以近乎实时地监控运行和配置的变化。
- 回退机制：当请求失败，执行回退逻辑，回退逻辑由开发人员自己提供，例如一个缺省值。
- 自我修复：熔断一段时间后，（放入一个请求，试试可以成功么，可以就打开）会自动进入半开状态。



- 舱壁模式

![](https://www.helloimg.com/images/2022/09/21/Z8aTvu.png)

> 为了避免问题服务请求过多导致正常服务无法访问，Hystrix不是采用增加线程数，而是单独的为每一个控制方法创建一个线程池的方式，这种模式叫做“舱壁模式”，也是线程隔离的手段。



- Hystrix的工作流程

1. 当调用出现问题时，开启一个时间窗（10s）

2. 在这个时间窗口内，统计调用次数是否达到最小请求数？

   如果没有达到，则重置统计信息，回到第1步

   如果达到了，则统计失败的请求数占所有请求数的百分比，是否达到阈值？

   如果达到，则跳闸（不再请求对应服务）

   如果没有达到，则重置统计信息，回到第1步

3. 如果跳闸，则会开启一个活动窗口，（默认5秒），每隔5秒，Hystrix会让一个请求通过，达到问题服务器，看是否调用成功，如果成功回到第一步，重置断路器，如果失败回到第3步。



### 源码

![](https://www.helloimg.com/images/2022/09/21/Z81Mb0.png)

通过启动类上而注解，扫描所有标记了@HystrixCommand注解的类，给每个创建了而HystrixCommand类，

![](https://www.helloimg.com/images/2022/09/21/Z812Im.png)

又通过jar包中的spring.factories文件中配置的HystrixCircuitBreakerConfiguration在启动时自定义启动项，创建上面扫描的hystrixCommand类的AOP切面。





## Feign远程调用

> Feign是Netflix扥啊的一个轻量级RESTful的HTTP服务客户端（用它来发起请求，远程调用用的）。以java注解的方式调用http请求，而不用像java中通过封装HTTP请求报文的方式直接调用。



- 超时重试机制

Feign本身已经集成了Ribbon依赖和自动配置，因此我们不需要额外导入依赖，

Feign默认的请求处理超时时长是1s，如果想更加可以手动修改超时时长



```yaml
#针对的被调⽤⽅微服务名称,不加就是全局⽣效
ribbon:
#请求连接超时时间
#  ConnectTimeout: 2000
#请求处理超时时间
#ReadTimeout: 5000
#对所有操作都进⾏重试
  OkToRetryOnAllOperations: true
  ####根据如上配置，当访问到故障请求的时候，它会再尝试访问⼀次当前实例（次数由MaxAutoRetries配置），
  ####如果不⾏，就换⼀个实例进⾏访问，如果还不⾏，再换⼀次实例访问（更换次数由MaxAutoRetriesNextServer配置），
  ####如果依然不⾏，返回失败信息。
  MaxAutoRetries: 0 #对当前选中实例重试次数，不包括第⼀次调⽤
  MaxAutoRetriesNextServer: 0 #切换实例的重试次数
  NFLoadBalancerRuleClassName:
  com.netflix.loadbalancer.RoundRobinRule #负载策略调整
```



- 日志

```java
// Feign的⽇志级别（Feign请求过程信息）
// NONE：默认的，不显示任何⽇志----性能最好
// BASIC：仅记录请求⽅法、URL、响应状态码以及执⾏时间----⽣产问题追踪
// HEADERS：在BASIC级别的基础上，记录请求和响应的header
// FULL：记录请求和响应的header、body和元数据----适⽤于开发及测试环境定位问题
@Configuration
public class FeignConfig {

    @Bean
    Logger.Level feignLevel(){
        return Logger.Level.FULL;
    }
}
```



### 源码

![](https://www.helloimg.com/images/2022/09/21/Z81plo.png)

通过启动类的@EnableFeignClients注解，启动了FeignClientsRegistrar类

此类主要负责把所有标记了@FeignClient的类的配置信息注入到Spring容器中

根据配置的信息，去注册中心拿详细的信息，然后通过JDK动态代理来创建详细的代理类

我们每次调用时，实际上调用的Feign使用JDK动态代理创建好的代理类，里面封装了ribbon的负载均衡和RestTemplate调用远程服务。

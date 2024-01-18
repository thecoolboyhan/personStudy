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

# GateWay

> spring gateWay天生就是非阻塞的，基于Reactor模型。

- 路由：网关最基础的部分，把一个请求路由到目标服务器
- 断言：类似java8的断言，真或假的校验，如果断言和请求相匹配就断言。
- 过滤器：请求之前或者之后，执行业务代码。



## 动态路由

通过配置让gateway路由到指定的服务名称上，然后通过注册中心访问对应的单个机器。



## gateway高可用

启动多个网关实例，在请求到达网关之前先使用Nginx来把请求负载到不同的gateway上来实现高可用。



# 分布式链路追踪Sleuth+Zipkin



## Sleuth的几个基本概念

- Trace：服务追踪的追踪单元，从客户发起求抵达被追踪系统的边界开始，到被追踪系统给客户端返回响应为止的过程。
- Trace ID：每一次追踪的唯一表示。
- Span ID：为了统计各处理单元的时间延迟，统计处每个单元的调用开始时间戳，和结束时间戳。还有时间名称和请求信息等。

## ZipKin

> 负责配合sleuth来分析sleuth记录的日志，需要有一个server端。每个配置了zipkin的服务都需要把数据传输给zipkin。

- zipkin的配置

默认一次记录百分之十的数据。

客户端传输给zipkin服务端时，建议使用mq来传输，减轻网络压力。





# 分布式统一认证 OAuth2+JWT

## 微服务下的统一认证方案：

- 基于Session的认证方式

  每个用户信息都需要在session中存储用户信息，通过负载均衡将本地的请求分配到另一个服务器需要将session的信息鞋带过去，否则会重新认证。我们可以使用session共享（基于redis和spring-session，把原来放在tomcat中的session信息存放到redis中），Session粘贴（可以基于tomcat来配置自动复制所有session信息，或者通过nginx来实现固定用户的请求永远都负载到同一台机器的方案）等。

  缺点：session需要基于cookie来实现，现在的部分移动端已经不能使用cookie。

- 基于token的方式

  服务端不用存储认证数据，易维护扩展性强，客户端可以把token存在任意地方，并且可以实现web和app统一认证机制。

  缺点：token由于自己包含信息，一般数据量较大，而且每次请求都需要传递，因此比较占带宽。而且token的认证操作也会给服务器CPU带来额外的负担。



## OAuth2（开放授权）

- 简介：

> 允许用户授权第三方应用访问他们存储在另外的服务提供者上的信息，而不需要将用户名和密码提供给第三方应用或分享他们的所有内容。



- 流程

资源所有者：用户自己

客户端：我们想要登录的网站或应用。

认证服务器：我们已经登录的网站或者有一个专门的认证服务器。

资源服务器：我们之前已经登录的网站。



1. 资源所有者想要访问某个客户端，
2. 客户端请求资源所有者授权，资源所有者给此客户端授权，
3. 客户端收到资源所有者的授权许可向认证服务申请令牌。
4. 认证服务器认证授权许可返回Access Token
5. 客户端携带Access Token访问资源服务器
6. 资源服务器向认证服务器验证Access Token。
7. 验证通过后，给客户端返回受保护的资源



- 什么情况下使用OAuth2？

第三方授权登录的场景：我们经常登录一些网站或者应用的时候，可以使用第三方应用授权登录的方式。

单点登录的场景：比如项目中有很多微服务或者公司内部有很多服务，可以专门做一个认证中心（认证平台），所有的服务都需要到这个认证中心做认证，只做一次登录，就可以在多个授权范围内的服务中自由穿行。



- OAuth2的两种授权方式

1. 授权码：常用于qq等第三方登录的场景
2. 密码式（分布式常用）：提供用户名+密码换取token令牌。
3. 隐藏式：
4. 客户端凭证





- 认证细节

OAuth2给的token一共两种

access_token:平常用来做校验的token（有效期没有那么长）

refresh_token:当access_token过期时，服务器可以使用这个token去认证服务器重新获取一个access_token（这个token一般有效期比较长）



## JWT

> 用户的令牌中就携带着用户信息和过期时间，当请求资源服务器时，资源服务器不需要再去认证服务器做校验，直接在资源服务器来校验令牌。

- JWT的三个主要部分

头（header）：令牌的类型和使用的hash算法

负载（Payload）：内容是一个json对象，里面存放了具体的信息

签名（Signature）：防止JWT的内容被篡改



# 第二代springCloud核心组件（SCA）



# Nacos（服务注册，配置中心）

> 服务发现、配置管理和服务管理平台。

Nacos=Eureka+Config+Bus





## 健康检查

- 临时实例

临时实例只存在注册中心中，如果服务下线或者不可用会被注册中心删除，临时实例和注册中心保持心跳，如果注册中心一段时间没有收到临时实例的请求，注册中心会把它标记成不健康，然后在一段时间后进行删除。

- 持久实例

永久实例在被删除之前，会永远被保存在注册中心中，持久实例可能并不知道注册中心的存在，不会主动向注册中心上报心跳，这个时候就需要注册中心主动来确认。



## 保护阈值

> 可以设置为0-1之间的浮点数，为一个比例值（当前服务健康实例数/当前服务总实例数）

当前服务健康实例数/总实例数 < 阈值时，说明健康实例真的不多，这个时候保护阈值会被触发（状态为true），nacos会把所有的实例（健康的+不健康的）全部提供给消费者，消费者可能访问到不健康的实例请求失败。

> 这样也比造成雪崩好，虽然牺牲了一个请求，但还是保证了整个系统的可用性。



## 负载均衡

Nacos客户端引入的时候，会关联引入Ribbon的依赖包，我们使用OpenFeign的时候会引入Ribbon的依赖，Ribbon包括Hystrix达按照原来的方式配置即可。



## Nacos数据模型（领域模型）



- namespace：命名空间，对不同的环境进行隔离，比如隔离开发环境，测试环境和生产环境。
- Group：分组，将若干个服务或者若干个配置集归为一组，通常习惯一个系统归为一个组
- Service：某一个服务，比如简历微服务。
- DataId：配置集或者可以认为是一个配置文件。





# Sentinel分布式系统流量防卫兵

> 一个云原生微服务的流量控制、熔断降级组件。



- hytrix

服务消费者----》 服务提供者

1. 需要自己搭建监控平台dashboard
2. 没有提供UI界面来提供服务熔断，降级等配置。（需要写代码，侵入系统）

- Sentinel

1. 有单独可部署的dashboard，不需要自己创建项目，直接下载jar包启动就可以。
2. 减少代码开发，通过UI界面配置就可以完成细颗粒度配置。





**sentinel主要分两部分 **

- 核心库：java客户端，不依赖任何框架库，能够运行于所有java运行时环境，同时对Dubbo/SpringCloud环境有较好的支持。
- 控制台：Dashboard，基于Springboot开发，打包后可直接运行，不要额外的tomcat等应用容器。


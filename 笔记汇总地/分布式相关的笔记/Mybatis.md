## mybatis二级缓存



- 一级缓存

一级缓存是每个sqlSession对象独有的。提交或清空后就会失效。

- 二级缓存

二级缓存和一级缓存原理一样，第一次查询，会将数据放入缓存中，然后第二个查询则会直接去缓存中去，但是一级缓存是基于sqlSession的，而耳机缓存是基于mapper文件的namespace的，也就是说多个sqlSession可以共享一个mapper中的二级缓存区域，并且如果两个mapper的namespace相同，即使是两个mapper，那么这两个mappper中执行sql查询到的数据也将存放在相同的二级缓存区域中。

- mybatis与redis整合

> mybatis提供了一个cache接口，如果要实现自己的缓存逻辑，实现cache接口开发即可。mybatis提供了一个针对cache接口的redis实现类，该类存在mybatis-redis包中。



- 执行顺序

二级缓存->一级缓存->数据库



### 二级缓存源码解析

- 解析map的过程

首先创建currentCache对象，然后在解析mapper.xml过程过程中，（此过程是创建MappedStatement对象的过程），利用构建者模式把二级缓存对象，是否刷新二级缓存等信息一起放入MappedStatement对象中。



- TransactionalCacheManager（事务管理器）

> 二级缓存是从MappedStatement中获取的。由于MappedStatement存在于全局配置中，可以多个CachingExecutor获取到，这样就会出现线程安全问题。除此之外，若不加以控制，多个事务共用同一个缓存实例，会导致脏读问题。

主要有四个成员变量：

```java
//委托的 Cache 对象。 实际上，就是二级缓存 Cache 对象。
private final Cache delegate;
/**
 * 提交时，清空 {@link #delegate}
 * 初始时，该值为 false
 * 清理后{@link #clear()} 时，该值为 true ，表示持续处于清空状态
 */
private boolean clearOnCommit;
/**
 *  // 在事务被提交前，所有从数据库中查询的结果将缓存在此集合中
 */
private final Map<Object, Object> entriesToAddOnCommit;
/**
 *   在事务被提交前，当缓存未命中时，CacheKey 将会被存储在此集合中
 */
private final Set<Object> entriesMissedInCache;
```



- 过程：

1. sqlSession会调用到对应的MappedStatement对象，MappedStatement对象对检测是否调用cacheExecutor执行器，如果MappedStatement属性中二级缓存启用，执行cacheExecutor。判断此sql是否需要刷新cache缓存。尝试从二级缓存中获取结果，如果没有，执行查询操作。此查询操作会先去一级缓存中查询是否有此缓存，如果没有，查库把结果放到一级缓存中，把结果放入TransactionalCacheManager事务缓存管理器中。当此sqlSession发送commit或者close方法中，TransactionalCacheManager才会把事务缓存管理器中的数据放到二级缓存中。



# 延迟加载

> 就是在需要用到数据时才进行加载，不需要用到数据时就不加载数据，延迟加载也称为懒加载。

- 优点

先从单表查询，需要时再从关联表查询。大大提高了数据库的性能，因为查询一张表要比关联查询多张表速度快。

- 缺点

因为只有需要用到数据时，才会进行数据库查询，这样在大批量数据查询时，因为查询工作也要消耗时间，所以可能造成用户等待时间变长，造成用户体验下降。

- 在多表中

一对多，多对多：通常情况下采用延迟加载

一对一（多对一）：通常情况下采用立即加载。

- 主意：

延迟加载是基于嵌套查询来实现的。



### 原理

> 延迟加载是通过动态代理实现的，当调用需要延迟加载的指定对象的指定方法时，执行动态生成的代理对象的方法，去数据库中查询延迟数据。






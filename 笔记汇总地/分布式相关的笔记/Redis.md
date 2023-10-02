# 拉钩的redis

## 数据结构

### 基本数据类型

- string字符串

- list
- set集合
- sortedset有序集合
- hash类型（散列表）
- bitmap位图类型
- geo地理位置类型
- stream数据流类型

### redisDB结构

redis会在初始化时，会预先分配16个数据库

- redisDB的具体结构：

``` c++
typedef struct redisDb{
    int id;      //id数据库序号，为0-15（默认redis有16个数据库）
    long avg_ttl;//存储数据库对象的平均ttl（time to live），用于统计
    dict *dict;//存储所有的key-value
    dict *blocking_keys;//blpop存储阻塞key和客户端对象
    dict *ready_keys;//阻塞后push响应阻塞客户端，存储阻塞后push的key和客户端对象
    dict *wathced_keys;//存储watch监控的key和客户端对象
}
```



### redis的7种type

#### 字符串 SDS

![](https://gitee.com/grsswh/drawing-bed/raw/master/image/2023-9-412:55:27-1693803326815.png)

``` c
struct sdshdr{
 //记录buf数据中已使用字节的数量
    int len;
    //记录buf数组中未使用字节的数量
    int free;
    //字符数组，用于保存字符串
    char buf[];
}
```

优势：

1. 通过len和free可以用O（1）的时间复杂度来获取字符串的长度（c语言是O(n)）
2. 因为已经记录了长度，所以可以在可能会发生缓冲区溢出时自动重新分配内存。



#### 跳跃表（重点)

跳跃表是有序集合（sorted-set）的底层实现，效率高，实现简单。

- 思想：

> 将有序链表中的部分节点分层，每层都是一个有序链表。

- 查找：

优先从最高层开始向后查找，当到达某个节点时，如果next节点值大于要查找的值或next指针指向null，则从当前节点下降一层继续向后查找。

举例：

![](https://gitee.com/grsswh/drawing-bed/raw/master/image/2023-9-512:17:01-1693887420857.png)

> 类似于2分查找

- 插入和删除：

先从最下层开始构建，除开始和结束节点，每向上一层，都要有1/2的几率解决是否构建当前元素。

删除：

在每一层都找到指定元素，删除需要删除的元素。

- 跳表的特点：

1. 每层都是一个有序链表
2. 查找次数近似于层数的1/2
3. 最底层包含所有元素
4. 空间复杂度O(n)扩充了一倍

- Redis的跳表实现：

``` c
typedef struct zskiplistNode{
    sds ele;//存储字符串类型数据
    double score;//存储排序的分值
    struct zskiplistNode *backward;//后退指针，指向当前节点最底层的前一个节点
    //层，柔性数组，随机生成1-64的值
    struct zskiplistLevel{
        struct zskiplistNode *forward;//指向本层下一个节点
        unsigned int span;//本层下一个节点到本层的元素个数
    } level[];
} zskiplistNode;

//链表
typedef struct zskiplist{
    //表头节点和为节点
    struct zskiplistNode *header, *tail;
    //表中节点的数量
    unsigned long length;
    //表中层数最大的节点的层数
    int level;
}zskiplist;
```

结构示意图：

![](https://gitee.com/grsswh/drawing-bed/raw/master/image/2023-9-512:30:46-1693888245130.png)



#### 字典（重点+难点）

字典dict又称散列表（hash），用来存储键值对的一种数据结构。

Redis整个数据库是用字典来存储的。

对redis进行curd其实就是对字典数据进行curd。



- 数组：数据量少时，使用数组加偏移量的方式来存储对象，可以O(1）时间复杂度来获取对象。

如果数据变多时，还是需要使用到hash表



- hash：

redis解决hash冲突时，采用拉链法来处理。

情况类似于java的hashmap（没有树化）

- 渐进式rehash

扩容时需要rehash，但当数据特别大时，rehash是一个非常缓慢的过程，所以需要进行优化。

服务器忙，则只对一个节点进行rehash。

服务器闲，可批量rehash（100个节点）

字典的应用场景：

1. 数据库存储数据
2. 散列表对象
3. 哨兵模式的主从节点管理



#### 压缩列表

> 由一系列特殊编码的连续内存块组成的顺序型数据结构。

- 结构：



![](https://gitee.com/grsswh/drawing-bed/raw/master/image/2023-9-613:26:19-1693977978141.png)

zibytes:压缩列表的字节长度

zltail：压缩列表的尾元素相对于起始地址的偏移量

zlien：压缩列表的元素个数

entry1..entryx：压缩列表的各个节点

zlend：压缩列表的结尾，占一个字节，恒为0xFF（255）

- entry的编码结构：

![](https://gitee.com/grsswh/drawing-bed/raw/master/image/2023-9-614:13:54-1693980833194.png)

previous_entry_length：前一个元素的字节长度

encoding：表示当前元素的编码

content：数据内容

``` c
typedef struct zlentry{
    unsigned int prevrawlensize;  //previous_entry_length字段的长度
    unsigned int prevrawlen;	//previous_entry_length字段存储的内容
    unsigned int lensize;		//encoding字段的长度
    unsigned int len;			//数据内容长度
    unsigned int headersize;	//当前元素的首部长度，即previous_entry_length字段长度与encoding字段长度之和。
    unsigned char encoding;		//数据类型
    unsigned char *p;			//当前元素首地址
}zlentry;
```



- 应用场景：

sroted-set和hash元素个数少且是小整数或短字符串（直接使用）

list用快速链表（quicklist）数据结构存储，而快速链表是双向列表和压缩列表的组合（间接使用）



#### 整数集合（intset)

> 有序的（整数升序），存储整数的连续存储结构。

当Redis集合类型的元素都是整数并且都处在64位有效符号整数范围内，就使用该结构存储。



![2023-9-1516:11:05-1694765465742.png](https://gitee.com/grsswh/drawing-bed/raw/master/image/2023-9-1516:11:05-1694765465742.png)

``` c
typedef struct intset{
    uint32_t encoding;	//编码方式
    uint32_t length;	//集合包含的元素数量
    int8_t contents[];	//保存元素的数组
}
```

- 应用场景：

可以存储整数值，并且保证集合中不会出现重复元素。



#### 快速列表（重点）

> 快速列表（quicklist）是Redis底层重要的数据结构。是列表的底层实现。（Redis 3.2之前，Redis采用双向链表和压缩列表实现）。在Redis 3.2 之后，结合双向链表和压缩列表的优点，设计出了qucklist。

快速列表是一个双向链表，链表中的每个节点是一个压缩列表结构。每个节点的压缩列表都可以存储多个数据元素。

![2023-9-1516:26:51-1694766411320.png](https://gitee.com/grsswh/drawing-bed/raw/master/image/2023-9-1516:26:51-1694766411320.png)



- 数据结构：

``` c
typedef struct quicklist{
    quicklistNode *head;		//指向quicklist的头部
    quicklistNode *tail;		//指向quicklist的尾部
    unsigned long count;		//列表中所有元素项个数的总和
    unsigned int len;			//quicklist节点的个数，即ziplist的个数
    int fill : 16;				//ziplist大小限定，由list-max-ziplist-size 给定（Redis设定）
    unsigned int compress: 16; //节点压缩深度设置，由list-compress-depth给定（redis设定）
}quicklist;
```



``` c
typedef struck quicklistNode{
    struct quicklistNode *prev;	//指向上一个ziplist节点
    struct quicklistNode *next;	//指向下一个ziplist节点
    unsigned char *zl;			//数据指针，如果没有被压缩，就指向ziplist结构，反之指向qucklistLZF结构。
    unsigned int sz;			//指向ziplist结构的总长度（内存占用长度）
    unsigned int count : 16;	//表示ziplist中数据项个数
    unsigned int encoding : 2;	//编码方式，1--ziplist，2--quicklistLZF
    unsigned int container : 2;	//预留字段，存放数据的方式，1--NONE，2--ziplist
    unsigned int recompress : 1;//解压标记，当查看一个被压缩的数据时，需要暂时解压缩，标记此参数为1之后再重新进行压缩。
    unsigned int attempted_compress : 1;	//测试相关
    unsigned int extra : 10;	//扩展字段，暂时没用
    
}quicklistNode;
```



- 数据压缩

quicklist每个节点的实际数据存储结构为ziplist，这种结构的优势在于节省存储空间。为了进一步降低ziplist的存储空间。还可以对ziplist进行压缩。Redis采用的压缩算法是LZF。其基本思想是：数据与前面重复的记录重复位置及长度。不重复的记录原始数据。



> 压缩过后的数据可以分成多个片段，每个片段有两个部分，











## 过期和淘汰策略

- maxmemory

  redis服务器物理内存的最大值，如果达到maxmemory设定的值，通过缓存淘汰策略，从内存中删除对象


# redis高可用方案

## 主从同步

- redis2.8之前

1. 从服务器发送SYNC命令给主服务器

2. 主服务器生成RDB文件给从服务器，同时发送所有写命令给从服务器

3. 从服务器清空之前的数据，读取RDB文件
4. 通过命令传播的形式保持数据一致性

> 如果同步过程中断掉，主服务器重新生成RDB文件和mast命令给从服务器，从服务器重新恢复

- redis 2.8之后

1. redis主从同步，分为全量同步和增量同步
2. 从服务器第一次连接上主机是全量同步
3. 断线重连可能触发全量同步也可能是增量同步
4. 除此之外的情况都是增量同步



## 哨兵模式

Sentinel是一个特殊的Redis服务器

不会进行持久化

每个Sentinel启动后，会创建两个连向主服务器的网络连接

命令连接：用于向主服务器发送命令，并接收相应

订阅连接：用于订阅主服务器的--Sentinel----Hello频道



 Sentinel默认每十秒向主服务器发送info命令获取redis的信息

> Sentinel之间只创建命令连接，不创建订阅连接，因为Sentinel在通过订阅主服务器或者从服务器，就可以感知到新的Sentinel的加入。

- 检测主观下线状态

如果一个哨兵检测到主服务器没在down-after-milliseconds未响应

哨兵就认为该实例主观下线

向其他哨兵发送消息确认是否主观下线

当哨兵集群的选举数半数以上，该主就客观下线

### 故障转移

- 哨兵leader选举

> 当一个主服务器被判断为客观下线后，监视这个主服务器的所有Sentinel会通过选举算法（raft），选出一个Leader 哨兵去执行故障转移操作

- Raft

Raft一般有三种身份，主，跟随者，候选人。

Raft协议将时间分为一个一个任期（term），可以认为是一种逻辑时间

选举流程：

Raft采用心跳检测触发Leader选举

系统启动后，全部节点初始化为跟随者。term为0

节点如果接收到了请求投票命令或者附录命令，就会保持自己的跟随者身份

节点如果一段时间内没有收到附录消息，在该节点的超时时间内还没有发现Leader，跟随者会自动转换为候选人，开始竞选leader

- 竞选：

1. 增加自己的term
2. 启动新的定时器
3. 给自己投一票（每个节点只能投一票，候选人投给自己，跟随者投给收到的第一个发送投票命令的节点）
4. 向 所有其他节点发送投票命令，并等待其他节点回复

如果在计时超时前，节点收到多数节点的同意投票，就转换为Leader，同时向其他所有节点发送附录命令，告知自己成为Leader

- 故障转移

1. 把失效的主其中的一个从升级为新的主，并将原失效主和其他的从都改为复制新的主。
2. 当客户端连接原失效主失败时，集群会给客户端返回新的master，让客户端重新连接新的master
3. 主和从服务器切换后，原主和从的配置文件都会发生改变，也就是原主中会多一行复制新主服务器。



## 集群与分区

### 客户端分区

- 普通hash

客户端在存放数据的时候先做一个hash计算，根据结果来决定存到哪台redis服务器上

优点：

存放的数据key可以是中文或者字符串

热点数据分布均匀，不会存在哪一台机器上key特别多的情况

缺点：

扩展成本高，添加新redis服务器的时候，所有的数据都需要重新hash计算



- 一致性hash

普通的hash是对主机数取模，而一致性hash是对2^32^取模。我们把2^32^想象成一个圆，就像钟表一样。

再把redis服务器的hash（ip）对2^32取模， 确定redis服务器在这个圆上的位置。

存入的数据经过hash%2^32^之后，在圆上确定一个位置，当前点向右的第一个服务器就是存放此key 的位置。

当有新的redis服务器添加时，取圆上的位置。

服务器数据只需要修改新服务器圆上位置向右的第一个服务器的数据重新hash即可。

优点：

添加或移除节点时，数据只需要进行部分的迁移，其他服务器保持不变。



- 虚拟映射

如果上述hash环之后，数据还是分布不均匀，可以在圆上取服务器节点的对角，来映射重排数据，使数据分布均匀。

缺点：

复杂度高



### proxy端分区

codis豌豆荚提供的redis分区管理工具，推特也有一个叫TwemProxy。

codis在redis的基础上做了一层包装，引入了槽的概念。代理提供redis的分区功能。

- 缺点：

redis更新后，proxy也要跟着维护



### 官方cluster分区

> redis-cluster把所有的物理节点映射到（0-26383）个slot上，基本上采用平均分配和连续分配的方式。

添加一个新的节点时，会自动进行槽迁移，槽中的数据也会跟着移动。

cluster采用去中心化设计，每个主节点间都会互相通讯，就算客户端连接到错误的主节点，redis会转发到正确的节点。



## 容灾

- 故障检测

redis中每个节点都会给其他节点发送ping命令，如果一个节点ping另一个节点超时，他会给其他节点发送pfile命令，当有半数以上的节点都投出pfail票数后，则认为此节点故障。

- cluster失效的判断

1. 集群中半数以上的主节点都宕机（无法投票）
2. 宕机的主节点的从节点也宕机了（sloct槽分配不连续）

- 副本飘逸

如果一个master宕机，它只有一个从节点，从节点成为新的主节点，它没有从节点，会从其他从节点最多的一个主机点那移动一个从节点成为自己的从节点。



# 企业实战

## 缓存问题

### 缓存穿透

> 一般的缓存系统，都是按照key去缓存查询，如果不存在对应的value，就应该去后端系统查询（不如DB）。
>
> 缓存穿透是指在高并发下查询key不存在的数据（不存在的key），会穿过缓存查询数据库。导致数据库压力过大而宕机。



解决方案：

- 对查询结果为空的情况也设置缓存，缓存事件设置短一点，或者该key对应的数据insert了之后清除缓存。

  问题：缓存太多空值，占用了更多空间

- 使用布隆过滤器，在缓存之前再加一层布隆过滤器，在查询的时候先去布隆过滤器查询key是否存在，如果不存在就直接返回，存在再查询缓存和DB。（一个大数组，对key多次不同hash计算，记录此key分别出现在数组哪个位置，来判断缓存和数据库中是否有此key）

  如果数据库更新，布隆过滤器一定要添加

### 缓存雪崩

> 当缓存服务器重启或者大量缓存集中在某一时间段失效，这样在失效的时候，也会给后端系统带来很大的压力。（数据库崩溃）

解决方案：

1. key的失效期分散开，不同的key设置不同的有效期
2. 设置二级缓存（数据库不一定一致）
3. 高可用（脏读）



### 缓存击穿

对于一些设置了过期时间的key，如果这些key可能会在某些事件点被超高并发的访问，是一种非常“热点”的数据。这个时候需要考虑一个问题，缓存被“击穿”的问题，这个和缓存雪崩的区别在于这里针对某一key缓存，前者则是很多key。

> 缓存的某个时间点过期的时候，恰好在这个时间点对这个key有大量的并发请求过来，这些请求发现缓存过期一般都会从后端DB加载数据并回设到缓存，这个时候大并发的缓存可能会瞬间把DB压垮。

解决方案：

1. 用分布式锁控制访问的线程

   使用redis的setnx互斥锁进行判断，这样其他线程就处于等待状态，保证不会有大并发操作去操作数据库

2. 不设置超时时间，volatile-lru但会造成写一致问题

   当数据库数据发生更新时，缓存中的数据不会及时更新，这样会造成数据库中的数据和缓存中的数据的不一致，应用会从缓存中读取到脏数据，可采用延时双删策略处理。

### 数据不一致

- 保持数据最终一致性（延时双删）

1. 先更新数据库同时删除缓存项（Key），等读的时候再填充缓存。
2. 2秒后再删除一次缓存项（key）
3. 设置缓存过期时间比如十秒或者1小时
4. 将缓存删除失败记录到日志中，利用脚本提取失败记录再次删除（缓存失效期过长7＊24）。

升级方案

通过数据库的binlog来异步淘汰key，利用工具（canal）将binlog日志采集发送到mq中，然后通过ACK机制确认处理删除缓存



### Hot Key

> 当有大量的请求访问某个Redis某个Key时，由于流量集中达到网络上限，从而导致这个redis的服务器宕机。造成缓存击穿，接下来对这个key的访问将直接访问数据库造成数据库崩溃，或者访问数据库回填Redis再访问redis，继续崩溃。

- 如何处理热Key：

1. 变分布式缓存为本地缓存

   发现热key之后，把缓存取出后，直接加载到本地缓存中，可以采用Ehcache、Guava Cache都可以，这样系统在访问热key数据时就可以直接访问自己的缓存了。（数据不要求实时一致）

2. 在每个redis主节点上备份热Jey数据，这样可以在读取时采用随机读取的方式，将访问压力负载到每个redis上。

3. 利用对热点数据的限流，熔断保护措施

   每个系统实例每秒最多请求缓存集群读操作不超过400次，一超过就可以熔断掉，不让请求缓存集群，直接返回一个空白信息，然后用户稍后会自行再次重新刷新页面之类的。（首页不行，系统友好性差）

   通过系统层自己直接加限流熔断保护措施，可以很好的保护后面的缓存集群。

### Big Key

大key指的是存储的值（Value）非常大，常见场景：

- 热门话题下的讨论
- 大v的粉丝列表
- 序列化后的图片
- 没有及时处理的垃圾数据



大Key 的影响：

- 大key会大量占用内存，在集群中无法均衡
- Redis的性能下降，主从复制异常
- 在主动删除或过期删除时会操作时间过长而引起服务阻塞

如何发现大key：

1. redis-cli --bigkeys命令。可以找到某个实例5种数据类型（String,hash,list,set,zset）的最大Key。

   但如果redis的key比较多，执行该命令会比较慢。

2. 获取生产redis的RDB文件，通过rdbtools分析rdb生成csv文件，在导入MySQL或其他数据库中进行分析统计，根据size_in_bytes统计bigKey。



大key的处理：

1. String 类型的big Key，尽量不要存入Redis中，可以使用文档型数据库MongoDB或缓存到CDN上。

   如果必须redis存储，最好单独存储，不要和其他的key一起存储，采用一主一从或多从。

2. 单个简单的key存储的value很大，可以尝试将对象分拆成几个key-value，使用mget获取值，这样分拆的意义在于分拆单次操作的压力，将单次操作压力平摊到多次操作中，降低对redis的IO影响。

   hash、set、zset、list中存储过多的元素，可以将这些元素分拆（分页）。

3. 删除大Key时 不要使用del，因为del是阻塞命令，删除时会影响性能。

4. 使用lazy delete（unlink命令）undel

   删除指定的key（s），若key不存在则该key被跳过。但是，相比DEl会会产生阻塞，该命令会在另一个线程中回收内存，应为它是非阻塞的。这也是该命令名字的由来，仅将keys从key空间中删除，真正的数据删除会在后续异步操作。

## 分布式锁

> 分布式锁是控制系统之间的同步访问共享资源的一种方式。
>
> 利用redis的单线程特性对共享资源进行串行化处理。

### 利用Watch实现redis乐观锁

1. 利用redis的watch功能，监控这个rediskey 的状态值
2. 获取rediskey的值
3. 创建redis事务
4. 给这个key的值+1
5. 然后去执行 这个事务，如果key的值被修改过则回滚。

### 实现方式

- 使用set命令实现

``` java
public boolean getLock(String lockKey,String requestId,int expireTime){
    //setnx:查询是否有这个key存在，如果没有就设置成功，如果有就不能设置
    //ex：过期时间，如果上面设置的key没有被当前线程手动删除，到达过期时间此key自动失效（删除）
    String result= jedis.set(lockKey,requestId,"NX","EX",expireTime);
    if("OK".equals(result)){
        return true;
    }
    return false;
}
```

释放锁：

``` java
public static void releaseLock(String lockKey,String requestId){
    if(requestId.equals(jedis.get(lockKey))){
        jedis.del(lockKey);
    }
}
```

> 问题在于如果调用jedis.del()方法的时候，这把锁已经不属于当前客户端的时候会解除他人家的锁。比如客户端A加锁，一段时间后客户端A解锁，在执行jedis.del()之前，锁突然过期了，客户端B尝试加锁成功。然后客户端A在执行del方法，则将客户端B的锁解锁。

- redis+lua脚本实现

> 因为lua脚本是原子性的，不存在拿到锁，再误删别人锁的情况。

``` java
public static boolean releasLock(String lockKey,String requestId){
    String script="if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
    Object result = jedis.eval(script, Collections.singletonList(lockKey),Collections.singletonList(requestId));
    if (result.equals(1L)){
        return true;
    }
    return false;
}
```



### 存在的问题

无法保证强一致性，在主机宕机的情况下会造成锁的重复获取。

> A在主获得锁，setnx，此时还没有触发主从同步，redis主节点挂了，从变为主，b在新主节点上又获得锁，此时就有两个线程同时获得了同一把锁。

- redLock

不要只在一个主节点上获取锁setnx，至少在半数以上的节点通过setnx成功后才能获取锁。



#### Redission分布式锁的使用

> Redission是架设在Redis基础上的一个java驻内存数据网络。
>
> Redission在基于NIO的Netty框架上，生产环境使用分布式锁。

![](https://www.helloimg.com/images/2022/04/29/RMp24A.png)

- watch dog

后台有一个线程，会每隔10秒检查一下，如果客户端1还持有锁key，那么就会不断的延长锁key的生存时间。

# 大厂面试提

## 缓存雪崩，缓存穿透，缓存击穿

穿透： 不存在的key

雪崩：大量的key失效

击穿： 一个key或一些key 热点数据

## 数据一致性问题

- 延时双删

不是实时一直，而是最终一致。

如果延时双删不成功，就等key失效。



- 热点数据和冷数据

热点数据怎么处理

冷数据如果淘汰过期



## redis为什么这么快

- redis在内存中操作，正常情况下和硬盘不会频繁swap
- maxmemory的设置+淘汰策略
- 数据结构简单，有压缩处理，专门设计的
- 单线程没有锁，没有多线程的切换和调度，不会死锁，没有性能消耗
- 使用i/o多路复用模型，非阻塞io
- 构建了多种通讯模式，进一步提升了性能

## 如何在多个核心的CPU上利用redis的性能

redis是单线程的，如果想在多个CPU或者多个核心上充分利用redis性能，

在单个机器上部署多个redis实例，然后使用taskset指令将不同的redis绑定到不同的核心上。


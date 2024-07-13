





# Netty介绍

## 为什么使用netty

### 异步设计

> 整个netty的API都是异步的

异步处理提供更有效的处理资源，它允许你创建一个任务，当有事件发生时，将获得通知，并等待事件完成。这样就不会阻塞，不管事件完成与否都会及时返回，资源利用率更高，程序可以利用剩余的资源做一些其他的事情。



- CallBacks（回调）

> 一个回调是被传递到并且执行完该方法。

工作线程，给被调用线程传递一个可以回调自己的对象。当被调用线程正常返回，或者出现异常，都会回调工作线程，来返回答案。



- Futures

> 一种抽象的概念，表示一个值，该值可能在某个点变得可用。一个Future要么获得计算完的结果，要么获得计算失败后的异常。

主线程工作分给两个线程，接收线程池返回的future对象，之后主线程不断的判断两个工作线程是否已经结束，如果一个结束了处理，从另一个线程获取返回值。



## java中的IO

- BIO和NIO

bio：一个客户端连接都要对应一个服务器线程处理。这样需要在线程池创建大量的线程数。



### ByteBuffer

> 无论是NIO还是Netty，bytebuffer都至关重要。byteBuffer可以直接在堆上和堆外分配内存。

可以直接分配得到buffer，能更快的将其中的内容写入channel，但是分配/回收代价高昂。



byteBuffer常见的操作

1. 向byteBuffer中写入数据
2. 调用byteBuffer.flip()方法，在读写模式间切换
3. 从bytebuffer中读出数据
4. 调用byteBuffer.clear或者byteBuffer.compact()

往byteBuffer中写入数据时，它会通过修改write下标位置，来跟踪当前已经写入了多少数据内容。

需要读取数据时，调用2方法，从写模式切换到读模式。

把limit下标移动到已经写入数据的当前位置，把position下标变为0，这样就可以读byteBuffer中的数据了。



> byteBuffer是双向的，支持读和写



- byteBuffer的两种类型

DirectByteBuffer: 

> 使用操作系统级别的内存，分配比较慢，但是数据的读写比较快，因为少了一次从系统内存到JVM的复制过程。

初始化方法：

``` java 
ByteBuffer directByteBuffer = ByteBuffer.*allocateDirect*(1024 * 4);
```



HeapByteBuffer

> 使用JVM的堆内存，对于JVM来说，分配比较快，但是读写慢，因为需要将操作系统里的数据复制到JVM内存。

初始化方法：

``` java
ByteBuffer heapByteBuffer = ByteBuffer.allocate(1024 * 4);
```



- 核心属性

  - capacity:

    ByteBuffer的容量，这个值在ByteBuffer初始化的时候就确定下来，不论是在读还写模式下，这个值都不变。

  - position：

    写模式：表示当前写到了byteBuffer的哪个位置，byteBuffer初始化时，这个值是0。

    postion的最大值为capacity-1。

    读模式：当从写切换到读模式，会将postion置为0，即从byteBuffer的起始位置开始读取数据。

  - limit：

    写模式：表示最大可写入的数据量，byteBuffer的最大容量，值为capacity

    读模式 ：当从写模式切换到读模式，limit会被设置为读模式下的position值，最大可读取量。



- 核心方法

  - flip()

    > 将写模式切换到读模式

    会触发对核心属性的操作

    把position设置为0，也就是byteBuffer的起始位置。

    将limit设置为写模式下的position，也就是最大可读取数据量。

  - mark（）

    标记当前position的位置。

  - reset（）

    把position指向上一次mark的位置。可以从这个位置重复向下读取数据。

  - clear（）

    > 在逻辑上清空byteBuffer的数据，实际上不清空。

    将limit设置为capacity

    postion设置为0

    实际上不清空数据，只是下次写入时从0开始写数据。

    如果byteBuffer并没有完全读完，调用此方法可以忽略那些没有读取的数据。

  - compact（）

    如果没有读取完byteBuffer的数据，调用compact（）将会把position-limit的数据（未读取的数据）拷贝到byteBuffer的起始位置。并且position为剩余数据量的大小，下次再往byteBuffer写入数据时，将会继续写入。不会覆盖。

  - hasRemaining（）

    判断缓冲区中是否还有未读数据

- 将数据写入byteBuffer的方式

``` java
byteBuffer.put(x);
channel.read(byteBuffer);//从通道上读取数据，写入缓冲区
```

- 从byteBuffer中读取数据的方式

``` java
byteBuffer.get();
channel.write(byteBuffer);//从缓冲区读取数据，写入通道
```







### selectors

Channel将连接connection，关联到可以进行IO操作的实体上，如文件或socket

Selector是个NIO组件，可以检测哪些Channel可用于读/写操作。一个selector可以处理多个连接。



- selector的工作流程

1. 创建一个或者多个selector，用于注册那些opened的channel
2. channel注册后，需要初始化你需要监听哪些事件。
3. 完成channel注册，可以调用selector.select()，该方法会发生阻塞，直到监听的某个事件发生。
4. 方法不再阻塞时，可以调用各种selectionKey的实例（该实例可以关联到注册过的channel级选中的操作项）去做逻辑操作。。。



## NIO面临的问题和解决方案

- java的NIO的缺陷

NIO在linux和win上运行的效果不一样。两个平台代码不一定通用。

NIO2只支持JDK7之后的版本，而且不支持UDP协议。NIO2只为TCP量身定做。



# 创建一个netty应用



- 创建一个简单netty服务器步骤

1. 创建ServerBootStrap实例，获取服务端引擎，然后做bind操作。
2. 创建NioEventLoopGroup实例，用于事件处理，如接入新连接，读写数据等。
3. 设置InetSocketAddress，绑定到服务端上。
4. 构建一个childHandler，用于处理接入的各个连接。
5. 调用serverBootstrap.bind()，绑定到server上。



netty 每个channel通过添加多个handler的方式，来控制channel的业务逻辑。每个handler最先被执行的是channelRead方法。





# 读《Netty in action》有感





# 第一部分、Netty的概念及体系结构


## 第一章、Netty--异步和事件驱动



### java早期BIO处理请求方式



早期java阻塞函数处理请求的方式：

```java
public void execute(int port) throws IOException {
//        创建一个新的ServerSocket,用来监听指定端口上的连接请求。
        ServerSocket serverSocket = new ServerSocket(port);
//        对accept()的调用将会被阻塞，直到一个连接建立。
        Socket clientSocket = serverSocket.accept();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        String request,response;
        while((request=in.readLine())!=null){
//            如果客户端传递了done表示处理结束，退出循环。
            if("Done".equals(request)) break;
//            处理被传递给服务器的处理方法
//            response=processRequest(request);
//            服务器给客户端响应处理结果
            out.println(request);
        }
    }
```

![2024-7-1015:46:08-1720597567759.png](https://gitee.com/grsswh/drawing-bed/raw/master/image/2024-7-1015:46:08-1720597567759.png)



上方代码只能同时处理一个连接，要管理多个并发客户端，需要为每个新的客户端Socket创建一个新的Thread。



- 缺点：

1. 大部分线程几乎都处于休眠状态。

2. 创建的每个线程都需要创建一个栈，每个栈至少都要盛情默认空间的内存。
3. 如果线程多，上下文切换成本高。



### java NIO

> new or Non-blocking
>
> NIO最开始是新的输入/输出（new Input/output)的英文缩写，但该API已经出现足够长的时间，不再是“新的”了，因此，如今大多数的用户认为NIO代表非阻塞I/O，而阻塞IO是旧的输入/输出。



- NIO的两个优化

1. 使用setsockopt()方法配置套接字，如果没有读写调用的时候就会立刻返回。
2. 可以使用操作系统的时间通知API注册一组非阻塞套接字，以确定它们中是否有任何的套接字已经有数据可供读写。（IO多路复用）。

![2024-7-1016:00:49-1720598448120.png](https://gitee.com/grsswh/drawing-bed/raw/master/image/2024-7-1016:00:49-1720598448120.png)

java.nio.channels.Selector是java的非阻塞IO实现的关键。它使用了事件通知API（IO多路复用）以确认一组非阻塞套接字中有哪些已经就绪能够进行IO相关的操作。因此可以在任何的时间检查任意的读写操作的完成状态。所以可以实现一个线程处理多个并发的连接。



- 优点：

1. 使用较少的线程可以处理许多的连接，减少了内存管理和上下文切换带来的开销。
2. 当没有IO操作需要处理的时候，线程也可以被用于其他任务。



### Netty简介



Netty的特性总结



| 分类     | Netty的特性                                                  |
| -------- | ------------------------------------------------------------ |
| 设计     | 统一的API，支持多种传输类型，阻塞和非阻塞的 \n 简单而强大的线程模型 \n 真正的无连接数据报套接字支持 \n 链接逻辑组件以支持复用 |
| 易于使用 | 详细的javadoc和大量的示例集 \n                               |
| 性能     | 拥有比java的核心API更高的吞吐量以及更低的延迟\n 得益于池化和复用，拥有更低的资源消耗\n 更小的内存复制 |
| 健壮性   | 不会因为慢速、快速或者超载的连接而导致OutOfMemoryError \n 消除在高速网络中NIO应用程序常见的不公平读写比率 |
| 安全性   | 完整的SSL/TLS 以及StartTLS支持 \n 可用于受限制环境下         |
| 社区驱动 | 发布快速而且频繁                                             |





- 异步和可伸缩性

异步：我们不必等待一个操作的完成。异步方法会立刻返回，并且在它完成时，会直接或稍后的某个时间点通知用户。

可伸缩性：一种系统、网络或者进程在需要处理的工作不断增长时，可以通过某种可行的方式或扩大它的处理能力来适应这种增长的能力。



#### Channel

它表示一个实体（一个硬件设备、一个文件、一个网络套接字或者一个能够执行一个或多个不同IO的操作程序组件）的开放连接，如读操作和写操作。



#### 回调

一个回调其实就是一个方法，一个指向已经被提供给另一个方法的方法引用。这使得被调用者在适当的时候可以回调调用者。是在操作完成后通知相关方常用的方式。



- 回调示例：

```java
/**
 * 当一个连接建立，ChannelHandler回调channelActive方法，打印出连接的地址。
 */
public class ConnectHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client "+ctx.channel().remoteAddress()+"connected");
    }
}
```



#### Future

Future是另一种在操作完成时通知应用程序的方式。这个对象可以看做是一个异步操作的结果占位符，它将在未来的某个时刻完成，并提供对其结果的访问。



- JDK预设的Future

只允许手动检查对应的操作是否已经完成，或者一直阻塞知道它完成。



- Netty的ChannelFuture

可以注册一个或者多个ChannelFutureListener

1. 监听的回调方法operationComplete()，会在对应的操作完成时被调用。
2. 通过ChannelFuture可以判断该操作是成功还是失败。
3. 通过ChannelFutureListener提供的通知机制，消除了手动检查对应的操作是否完成的必要。

每个IO操作都不会阻塞，所以Netty是完全异步和事件驱动的。



```java
/**
 * 演示利用ChannelFuture处理连接请求，和连接请求的结果。全程不影响其他代码。
 */
public class ChannelFuture1 {
    public void execute(Channel channel){
//        连接会异步的建立，不影响其他逻辑代码。
        ChannelFuture future = channel.connect(new InetSocketAddress("192.168.31.141", 25));
        //给此结果添加一个ChannelFutureListener处理方法，并根据返回结果来做出回应
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(future.isSuccess()){
                    ByteBuf buffer = Unpooled.copiedBuffer("Hello", Charset.defaultCharset());
                    ChannelFuture wf = future.channel().writeAndFlush(buffer);
                }else{
                    Throwable cause = future.cause();
                    cause.printStackTrace();
                }
            }
        });
    }
}
```



#### 事件和ChannelHandler



![2024-7-1115:46:48-1720684007367.png](https://gitee.com/grsswh/drawing-bed/raw/master/image/2024-7-1115:46:48-1720684007367.png)





#### 总结



Netty的异步编程模型建立在Future和回调的概念之上的，而将事件派发到ChannelHandler的方法则发生在更深的层次上。

拦截操作以及高速地转换入站数据和出站数据，都只需要你提供回调或者利用操作所返回的Future。这使得链接操作变得既简单又高效，并且促进了可重用的通用代码的编写。



Netty通过触发事件将selector从应用程序中抽象出来，消除了所有本来将需要手动编写的派发代码。



## 第二章、你的第一个Netty应用程序



### 不同的事件处理，读取数据的示例

```java
//此注解表示，当前channel-handler可以被多个Channel安全的共享
@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

//    每个传入的消息都会调用此方法
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in =(ByteBuf) msg;
//        用来读取消息，把消息都记录到控制台
        System.out.println("server received:"+in.toString(CharsetUtil.UTF_8));
//        把收到的消息写给发送者
        ctx.write(in);
    }

//    通知ChannelInboundHandler此消息为最后一条消息
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        将消息冲刷到远程节点，关闭Channel
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }
    
//    异常处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
```



- 如果不捕获异常，会发生什么？

每个Channel都拥有一个与之相关联的ChannelPipeline，其持有一个ChannelHandler的实例链。

在默认情况下，ChannelHandler会把它的方法的调用转发给链中的下一个ChannelHandler。因此，如果exceptionCaught（）方法没有被该链中的某处实现，那么所接收的异常将会被传递到ChannelPipeline的尾端并被记录。为此，你的应用程序至少与有一个实现了exceptionCaught（）方法的ChannelHandler。





### 服务端代码



```java
//服务端主启动类，用来创建channel，绑定通道，实例处理等
public class EchoServer {
    private final int port;
//    新建时指定当前服务的端口。
    public EchoServer(int port){
        this.port=port;
    }

    public static void main(String[] args) {
//        if(args.length!=1) System.out.println("Usage:"+EchoServer.class.getSimpleName()+"<port>");
//        int port=Integer.parseInt(args[0]);
        int port =8080;
        try {
//            启动服务器
            new EchoServer(port).start();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() throws InterruptedException {
        final  EchoServerHandler serverHandler=new EchoServerHandler();
//        创建事件处理线程池
        EventLoopGroup group=new NioEventLoopGroup();
        try {
//            服务器启动器
            ServerBootstrap b = new ServerBootstrap();
//            指定服务器使用哪个线程池
            b.group(group)
//                    指定所使用的channel
                    .channel(NioServerSocketChannel.class)
//                    设置端口号
                    .localAddress(new InetSocketAddress(port))
//                    指定一个EchoServerHandler到子channel的channelPipeline里
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
//                            上面创建的Handler被标记为@Shareable，可以重复使用相同的实例。
                            ch.pipeline().addLast(serverHandler);
                        }
                    });
//            sync()阻塞线程，直到完成绑定
            ChannelFuture f = b.bind().sync();
//            没有收到closeFuture就一直等待
            f.channel().closeFuture().sync();
        }finally {
//            关闭线程池，释放资源
        group.shutdownGracefully().sync();
        }
    }
}
```



1. EchoServerHandler实现了业务逻辑
2. main方法引导服务器
3. 创建一个ServerBootstrap的实例以引导和绑定服务器
4. 创建并分配一个NioEventLoopGroup实例以进行事件的处理，如接受新连接以及读写数据
5. 指定服务器绑定的本地的InetSocketAddress
6. 使用一个EchoServerHandler的实例初始化每一个新的channel
7. 调用ServerBootstrap.bind()方法来绑定服务器。



### 客户端代码



- 客户端事件处理代码

```java
@ChannelHandler.Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
//    在到服务器的连接已经建立之后被调用
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        建立连接后，发送一条消息！
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
    }

//    当从服务接收到一条消息后被调用
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println("Client received:"+msg.toString(CharsetUtil.UTF_8));
    }
//    出现异常时调用
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
```



- 引导客户端代码



```java
public class EchoClient {
    private final String host;
    private final int port;
    public EchoClient(String host,int port){
        this.host=host;
        this.port=port;
    }
    
    public void start() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host,port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    new EchoClientHandler()
                            );
                        }
                    });
            ChannelFuture f = b.connect().sync();
            f.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        String host = "";
        int port=10086;
        new EchoClient(host,port).start();
    }
    
}
```



1. 初始化客户端，创建一个bootstrap实例
2. 为事件处理分配一个nioEventLoopGroup实例，其中事件处理包括创建新的连接以及处理入站和出站数据
3. 为服务器连接创建一个InetSocketAddress实例。
4. 当连接被建立时，一个EchoClientHandler实例被安装到channelPipeline中。
5. 在一切都设置完后，调用bootstrap。connect（）方法连接到远程节点、



## 第三章、Netty的组件和设计



### Channel、EventLoop和ChannelFuture



Channel：socket

EventLoop：控制流、多线程处理、并发；

ChannelFuture：异步通知。



- Channel接口

用来处理socket连接



- EventLoop接口



![2024-7-1216:14:58-1720772097636.png](https://gitee.com/grsswh/drawing-bed/raw/master/image/2024-7-1216:14:58-1720772097636.png)



1. 一个EventLoopGroup包含一个或者多个EventLoop
2. 一个EventLoop在它的生命周期内只和一个Thread绑定
3. 所有由EventLoop处理的IO事件都将在它专有的Thread上被处理
4. 一个Channel在它的生命周期内只注册于一个EventLoop
5. 一个EventLoop可能会被分配给一个或多个Channel



> 一个给定的Channel的io操作都是由相同的Thread执行的，这样消除了对于同步的需要。



- ChannelFuture接口

一个操作可能不会立刻返回，ChannelFuture接口用来确认其结果。（无论成功还是失败）



### ChannelHandler和ChannelPipeline



- ChannelHandler接口

Netty的主要组件就是ChannelHandler，他是所有处理入站、出站数据的应用程序逻辑的容器。



- ChannelPipeline接口

ChannelPipeline提供了ChannelHandler链的容器，并定义了用于在该链上传播入站、出站事件流的API。当Channel被创建，它会自动被分配到它专属的ChannelPipeline。



> ChannelHandler安装到ChannelPipeline的过程：



1. 一个ChannelInitializer的实现被注册到ServerBootstrap中。
2. 当ChannelInitializer.initChannel()方法被调用，ChannelInitializer将在ChannelPipeline中安装一组自定义的ChannelHandler；
3. ChannelInitializer将它自己从ChannelPipeline中移除。

![2024-7-1223:26:27-1720797986303.png](https://gitee.com/grsswh/drawing-bed/raw/master/image/2024-7-1223:26:27-1720797986303.png)



- 编码器和解码器


package nettyInAcation.part2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

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

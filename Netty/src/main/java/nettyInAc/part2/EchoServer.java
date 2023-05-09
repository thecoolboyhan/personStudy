package nettyInAc.part2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class EchoServer {
    private final int port;
    public EchoServer(int port){
        this.port=port;
    }
    public void  start() throws InterruptedException {
        //创建一个基于Nio的处理连接的线程池
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            //创建一个netty服务器启动项
            ServerBootstrap b = new ServerBootstrap();
            //把当前nettyServer绑定注册到末上面创建的现场组，或者是selector组
            b.group(group)
                    //用当前连接组来控制socket连接，所以给此channel声明为NioServerSocketChannel
                    .channel(NioServerSocketChannel.class)
                    //给上面创建的服务器绑定端口号和地址
                    .localAddress(new InetSocketAddress(port))
                    //建立连接的时候，会调用下面添加的子channel，传入的ChannelInitializer为创建channel的工具类
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //channelPipeline：存放多个handler。
                            //EchoServerHandler：用于处理各种事件的拦截器
                            ch.pipeline().addLast(new EchoServerHandler());
                        }
                    });
            //ServerBootstrap绑定成功后，阻塞
            ChannelFuture f = b.bind().sync();
            System.out.println(EchoServer.class.getName()+
                    "started and listen on "+ f.channel().localAddress());
            f.channel().closeFuture().sync();
        }catch (Exception e){

        }finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        if(args.length!=1){
            System.err.println("打印一段话");
        }
        int port =Integer.parseInt(args[0]);
        new EchoServer(port).start();
    }

}

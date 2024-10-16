package nettyInAcation.part8;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

//共享EventLoop
public class ShareEventLoop {
    public void run(){
//        新建一个Server端引导
        ServerBootstrap bootstrap = new ServerBootstrap();
//        指定两个线程池
        bootstrap.group(new NioEventLoopGroup(),new NioEventLoopGroup())
//                指定处理的Channel
                .channel(NioServerSocketChannel.class)
//                当上述Channel触发时指定子事件
                .childHandler(
                        new SimpleChannelInboundHandler<ByteBuf>() {
                            ChannelFuture connectFuture;
//                            当连接成功后，触发此事件
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
//                                创建一个引导来连接远程服务器
                                Bootstrap bootstrap1 = new Bootstrap();
//                                给引导指定Channel
                                bootstrap1.channel(NioSocketChannel.class)
//                                        处理入站事件
                                        .handler(
                                                new SimpleChannelInboundHandler<ByteBuf>() {
                                                    @Override
                                                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                                                        System.out.println("Received data");
                                                    }
                                                }
                                        );
//                                给当前Channel指定为上次的childGroup
                                bootstrap1.group(ctx.channel().eventLoop());
//                                连接远程服务器
                                connectFuture= bootstrap1.connect(new InetSocketAddress("www.baidu.com", 80));
                            }
//                            接收数据事件
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                                if(connectFuture.isDone()) System.out.println("11");
                            }
                        }
                );
//        给服务器绑定端口号
        ChannelFuture future = bootstrap.bind(new InetSocketAddress(8080));
//        添加连接后的回调
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(future.isSuccess()) System.out.println("Server bound");
                else {
                    System.out.println("Bind attempt failed");
                    future.cause().printStackTrace();
                }
            }
        });
    }
}

package nettyInAcation.part8;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class BootStrapSever {
    public void run(){
//        创建Server端线程池
        NioEventLoopGroup group = new NioEventLoopGroup();
//        创建引导类
        ServerBootstrap bootstrap = new ServerBootstrap();
//        把线程池赋值给Server引导
        bootstrap.group(group)
//指定当前EventLoop的Channel
                .channel(NioServerSocketChannel.class)
//                每当Channel时间触发时，会新创建的子Channel
                .childHandler(new SimpleChannelInboundHandler<ByteBuf>() {
//                    子Channel的连接事件
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                        System.out.println("Received  Data");
                    }
                });
//        绑定服务器
        ChannelFuture future = bootstrap.bind(new InetSocketAddress(8080));
//        添加Channel完成的回调
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(future.isSuccess()){
                    System.out.println("Server bound");
                }else{
                    System.out.println("Bound attempt failed");
                    future.cause().printStackTrace();
                }
            }
        });
    }
}

package nettyInAcation.part8;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class BootStrapNIOTCPClient {
    public void  run(){
//        新建一个bootstrap来创建和连接新的客户端的Channel
        Bootstrap bootstrap = new Bootstrap();
//        创建一个用来处理Channel时间的EventLoopGroup，每个EventLoopGroup中，Channel的线程是固定的。
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
//        将Channel引导到EventLoopGroup中
        bootstrap.group(eventLoopGroup)
//                指定Channel事件的实现
                .channel(NioSocketChannel.class)
//                设置Channel入站事件和数据的ChannelBoundHandler
                .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
//                        接收数据
                        System.out.println("Received data");
                    }
                });
//        连接远程机器
        ChannelFuture future = bootstrap.connect(new InetSocketAddress("www.baidu.com", 80));
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(future.isSuccess()){
                    System.out.println("Connection established");
                }else{
                    System.out.println("Connection attempt failed");
                    future.cause().printStackTrace();
                }
            }
        });
    }
}

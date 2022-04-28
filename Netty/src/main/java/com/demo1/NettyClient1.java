package com.demo1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 韩永发
 *
 * @author hp
 * @Date 16:28 2022/4/20
 */
public class NettyClient1 {
  public static void main(String[] args) throws InterruptedException {
    //1.创建线程组
    EventLoopGroup group = new NioEventLoopGroup();
    //2.创建客户端启动助手
    Bootstrap bootstrap = new Bootstrap();
    //3 设置线程组
    bootstrap.group(group)
            .channel(NioSocketChannel.class)//4.设置客户端通道实现为Nio
            .handler(new ChannelInitializer<SocketChannel>() {       //5.创建一个通道初始化对象
              @Override
              protected void initChannel(SocketChannel socketChannel) throws Exception {
                //添加解码器
//                socketChannel.pipeline().addLast("MessageDecoder",new MessageDecoder());
                //添加编码器
//                socketChannel.pipeline().addLast("MessageEncoder",new MessageEncoder());
                //编解码二合一
                socketChannel.pipeline().addLast(new MessageCodec());

                //6.向pipeLine中添加自定义业务处理handler
                socketChannel.pipeline()
                        .addLast(new NettyClientHandler1());
              }
            });
    //7.启动客户端，等待连接服务器，同时将异步改为同步
    ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9999).sync();
    //8.关闭通道和关闭连接池
    channelFuture.channel().closeFuture().sync();
    group.shutdownGracefully();
  }
}

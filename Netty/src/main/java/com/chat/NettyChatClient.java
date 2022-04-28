package com.chat;

import com.demo1.MessageCodec;
import com.demo1.NettyClientHandler1;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

/**
 * 韩永发
 *
 * @author hp
 * @Date 18:04 2022/4/24
 */
public class NettyChatClient {
  private String ip;
  private int port;

  public NettyChatClient(String ip, int port) {
    this.ip = ip;
    this.port = port;
  }

  public void run() throws InterruptedException {
    EventLoopGroup group = null;
    try {
      //1.创建线程组
       group = new NioEventLoopGroup();
      //2.创建客户端启动助手
      Bootstrap bootstrap = new Bootstrap();
      //3 设置线程组
      bootstrap.group(group)
              .channel(NioSocketChannel.class)//4.设置客户端通道实现为Nio
              .handler(new ChannelInitializer<SocketChannel>() {       //5.创建一个通道初始化对象
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                  //添加解码器
                  socketChannel.pipeline().addLast(new StringDecoder());
                  //添加编码器
                  socketChannel.pipeline().addLast(new StringEncoder());

                  //6.向pipeLine中添加自定义业务处理handler
                  socketChannel.pipeline()
                          .addLast(new NettyChatClientHandler());
                }
              });
      //7.启动客户端，等待连接服务器，同时将异步改为同步
      ChannelFuture channelFuture = bootstrap.connect(ip, port).sync();
      Channel channel = channelFuture.channel();
      System.out.println("---------------"+channel.localAddress().toString().substring(1)+"------------------");
      Scanner scanner = new Scanner(System.in);
      while (scanner.hasNext()) {
        String msg = scanner.nextLine();
        //向服务器发送消息
        channel.writeAndFlush(msg);
      }

      //8.关闭通道和关闭连接池
      channelFuture.channel().closeFuture().sync();
    } finally {
      group.shutdownGracefully();
    }
  }

  public static void main(String[] args) throws InterruptedException {
    new NettyChatClient("127.0.0.1",9999).run();
  }
}

package com.chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * 韩永发
 *
 * @author hp
 * @Date 9:47 2022/4/22
 */
public class NettyChatServer {
  private int port;
  public NettyChatServer(int port){
      this.port= port;
  }

  public void run(){
    EventLoopGroup bossGroup =null;
    EventLoopGroup workerGroup =null;
    try {
      //1。创建bossGroup线程组，处理网络事件----连接事件
      bossGroup = new NioEventLoopGroup(1);
      //2  创建workerGroup线程组，处理网络事件---读写事件
      workerGroup = new NioEventLoopGroup();//这里默认值为cpu线程数的2倍
      //3 创建服务端启动助手
      ServerBootstrap serverBootstrap = new ServerBootstrap();
      //4. 设置  bossGroup线程组，和 workerGroup线程组
      serverBootstrap.group(bossGroup,workerGroup)
              .channel(NioServerSocketChannel.class)//5.设置服务端通道为Nio
              .option(ChannelOption.SO_BACKLOG,128)//6.参数设置
              .childOption(ChannelOption.SO_KEEPALIVE,true)//设置检测保持活动的通道
              .childHandler(new ChannelInitializer<SocketChannel>() {      //7. 创建一个通道初始化对象
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                  //添加解码器
                  socketChannel.pipeline().addLast(new StringDecoder());
                  //添加编码器
                  socketChannel.pipeline().addLast(new StringEncoder());
                  //添加自定义处理类
                  socketChannel.pipeline().addLast(new NettyChatServerHandler());
                }
              });
      //9.启动服务端，并绑定端口号，同时将异步设置为同步
      ChannelFuture future = serverBootstrap.bind(port);
      future.addListener(new ChannelFutureListener() {
        @Override
        public void operationComplete(ChannelFuture channelFuture) throws Exception {
          if (channelFuture.isSuccess()){
            System.out.println("端口连接成功");
          }else {
            System.out.println("端口绑定失败");
          }

        }
      });
      System.out.println("聊天室服务器启动成功");
      //10.关闭通道（这里的关闭，不是真正的关闭，而是监听通道关闭的状态）。和关闭连接池
      future.channel().closeFuture().sync();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
    }
  }

  public static void main(String[] args) {
    new NettyChatServer(9999).run();
  }
}

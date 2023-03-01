package com.demo1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;

/**
 * 韩永发
 *
 * @author hp
 * @Date 16:53 2022/4/20
 */
public class NettyClientHandler1 implements ChannelInboundHandler {
  /**
   * 通道就绪事件
   *
   * @param channelHandlerContext
   * @throws Exception
   */
  @Override
  public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
    ChannelFuture channelFuture = channelHandlerContext.writeAndFlush("你好，我是Netty客户端");
    //  由于有编码器，所以不需要在这里进行编码转换
//    ChannelFuture channelFuture = channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer("你好，我是Netty客户端", CharsetUtil.UTF_8));
    channelFuture.addListener(new ChannelFutureListener() {
      @Override
      public void operationComplete(ChannelFuture channelFuture) throws Exception {
        if (channelFuture.isSuccess()) {
          System.out.println("数据发送成功");
        } else {
          System.out.println("数据发送失败");
        }
      }
    });

  }

  /**
   * 通道读就绪事件
   *
   * @param channelHandlerContext
   * @param o
   * @throws Exception
   */
  @Override
  public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
    System.out.println("服务端发来的消息:"+o);
    //由于添加了解码器，消息可以直接用string的形式打印出来
//    ByteBuf byteBuf = (ByteBuf) o;
//    System.out.println("服务端发来的消息:" + byteBuf.toString(CharsetUtil.UTF_8));
  }

  @Override
  public void channelRegistered(ChannelHandlerContext channelHandlerContext) throws Exception {

  }

  @Override
  public void channelUnregistered(ChannelHandlerContext channelHandlerContext) throws Exception {

  }

  @Override
  public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {

  }

  @Override
  public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {

  }

  @Override
  public void userEventTriggered(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

  }

  @Override
  public void channelWritabilityChanged(ChannelHandlerContext channelHandlerContext) throws Exception {

  }

  @Override
  public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {

  }

  @Override
  public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {

  }

  @Override
  public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {

  }
}

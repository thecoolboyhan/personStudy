package com.demo1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * 韩永发
 *
 * @author hp
 * @Date 16:11 2022/4/20
 */
public class NettyServerHandler1 implements ChannelInboundHandler {

  /**
   * 通道读取事件
   *
   * @param channelHandlerContext
   * @param o
   * @throws Exception
   */
  @Override
  public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
    System.out.println("客户端发来的消息:"+o);
    //由于添加了解码器，消息可以直接用string的形式打印出来
//    ByteBuf byteBuf= (ByteBuf) o;
//    System.out.println("客户端发来的消息："+byteBuf.toString(CharsetUtil.UTF_8));
  }

  /**
   * 读取完毕事件
   *
   * @param channelHandlerContext
   * @throws Exception
   */
  @Override
  public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {
    channelHandlerContext.writeAndFlush("你好我是Netty服务端");
    //消息出栈
//    channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer("你好我是Netty服务端",CharsetUtil.UTF_8));
  }

  /**
   * 通道异常事件
   *
   * @param channelHandlerContext
   * @param throwable
   * @throws Exception
   */
  @Override
  public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
    throwable.printStackTrace();
    channelHandlerContext.close();
  }

  @Override
  public void channelRegistered(ChannelHandlerContext channelHandlerContext) throws Exception {

  }

  @Override
  public void channelUnregistered(ChannelHandlerContext channelHandlerContext) throws Exception {

  }

  @Override
  public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {

  }

  @Override
  public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {

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


}

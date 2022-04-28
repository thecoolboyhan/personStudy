package com.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 韩永发
 *
 * @author hp
 * @Date 18:35 2022/4/22
 */
public class NettyChatServerHandler extends SimpleChannelInboundHandler<String> {
  public static List<Channel> channelList=new ArrayList<>();

  /**
   * 通道就绪事件
   * @param ctx
   * @throws Exception
   */
  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    Channel channel = ctx.channel();
    //当有新的客户端连接时，就把通道放入集合
    channelList.add(channel);
    System.out.println("[Server]:"+channel.remoteAddress().toString().substring(1)+"在线.");
  }

  /**
   * 通道未就绪+channel下线
   * @param ctx
   * @throws Exception
   */
  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    Channel channel = ctx.channel();
    channelList.remove(channel);
    System.out.println("[Server]:"+channel.remoteAddress().toString().substring(1)+"下线了.");
  }

  /**
   * 异常处理事件
   * @param ctx
   * @param cause
   * @throws Exception
   */
  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    Channel channel = ctx.channel();
    //移除集合
    channelList.remove(channel);
    System.out.println("[Server]:"+channel.remoteAddress().toString().substring(1)+"异常.");

  }

  /**
   * 通讯读取事件
   * 
   * @param channelHandlerContext
   * @param s
   * @throws Exception
   */
  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
    //当前发送消息嗯通道，当前发送的客户端连接
    Channel channel = channelHandlerContext.channel();
    for (Channel channel1 : channelList) {
      //删除自生通道
      if (channel!=channel1){
        channel1.writeAndFlush("["+channel.remoteAddress().toString().substring(1)+"]:说"+s);
      }
    }
  }
}

package com.chat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 韩永发
 *
 * @author hp
 * @Date 18:41 2022/4/24
 */
public class NettyChatClientHandler extends SimpleChannelInboundHandler<String> {
  /**
   * 通道读取就绪事件
   *
   * @param channelHandlerContext
   * @param s
   * @throws Exception
   */
  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
    System.out.println(s);
  }
  
}

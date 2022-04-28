package com.demo1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 韩永发
 *
 * @author hp
 * @Date 9:28 2022/4/22
 */
public class MessageCodec extends MessageToMessageCodec {

  /**
   * 编码器
   * @param channelHandlerContext
   * @param o
   * @param list
   * @throws Exception
   */
  @Override
  protected void encode(ChannelHandlerContext channelHandlerContext, Object o, List list) throws Exception {
    System.out.println("编码器正在进行编码");
    String str= (String) o;
    list.add(Unpooled.copiedBuffer(str, StandardCharsets.UTF_8));
  }

  /**
   * 解码器
   * @param channelHandlerContext
   * @param o
   * @param list
   * @throws Exception
   */
  @Override
  protected void decode(ChannelHandlerContext channelHandlerContext, Object o, List list) throws Exception {
    System.out.println("正在进行消息解码。。。。。");
    ByteBuf byteBuf= (ByteBuf) o;
    //传递到下一个handler
    list.add(byteBuf.toString(StandardCharsets.UTF_8));
  }
}

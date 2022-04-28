package com.demo1;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 韩永发
 *
 * @author hp
 * @Date 18:02 2022/4/21
 */
public class MessageEncoder extends MessageToMessageEncoder {
  @Override
  protected void encode(ChannelHandlerContext channelHandlerContext, Object o, List list) throws Exception {
    System.out.println("编码器正在进行编码");
    String str= (String) o;
    list.add(Unpooled.copiedBuffer(str, StandardCharsets.UTF_8));
  }
}

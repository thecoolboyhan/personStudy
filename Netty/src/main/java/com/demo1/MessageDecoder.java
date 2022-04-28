package com.demo1;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 韩永发
 *
 * @author hp
 * @Date 17:47 2022/4/21
 */
public class MessageDecoder extends MessageToMessageDecoder {

  @Override
  protected void decode(ChannelHandlerContext channelHandlerContext, Object o, List list) throws Exception {
    System.out.println("正在进行消息解码。。。。。");
    ByteBuf byteBuf= (ByteBuf) o;
    //传递到下一个handler
    list.add(byteBuf.toString(StandardCharsets.UTF_8));
  }
}

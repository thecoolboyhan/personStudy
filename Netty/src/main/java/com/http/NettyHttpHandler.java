package com.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * 韩永发
 *
 * @author hp
 * @Date 19:39 2022/4/24
 */
public class NettyHttpHandler extends SimpleChannelInboundHandler<HttpObject> {
  /**
   * 连接就绪事件
   *
   * @param channelHandlerContext
   * @param httpObject
   * @throws Exception
   */
  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {
    String tuBiao= "/favicon.ico";
    //1.判断是不是http请求
    if (httpObject instanceof HttpRequest){
      DefaultHttpRequest request= (DefaultHttpRequest) httpObject;
      System.out.println("浏览器请求路径:"+request.uri());
      if (tuBiao.equals(request.uri())){
        System.out.println("图标不响应!");
        return;
      }
      //2.给浏览器进行响应
      ByteBuf byteBuf = Unpooled.copiedBuffer("Hello!我是Netty服务器", CharsetUtil.UTF_8);
      DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
      //3.设置响应头
      response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/html;charset=utf-8");
      response.headers().set(HttpHeaderNames.CONTENT_LENGTH,byteBuf.readableBytes());
      channelHandlerContext.writeAndFlush(response);

    }
  }
}

package nettyInAcation.part6;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

public class DemoChannelHandlerContext {
//    通过ChannelHandlerContext访问Channel
    public void getChannel(ChannelHandlerContext ctx){
//        获取与ChannelHandlerContext关联的Channel
        Channel channel = ctx.channel();
//        向Channel中写入数据
        channel.write(Unpooled.copiedBuffer("Netty in Action", CharsetUtil.UTF_8));
    }
//    通过ChannelHandlerContext访问Pipeline
    public void getChannelPipeline(ChannelHandlerContext ctx){
//        获取Pipeline
        ChannelPipeline pipeline = ctx.pipeline();
//        通过Pipeline写入缓冲区
        pipeline.write(Unpooled.copiedBuffer("Netty in Action",CharsetUtil.UTF_8));
    }
}

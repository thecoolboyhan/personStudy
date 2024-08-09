package nettyInAcation.part6;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

//自定义处理入站异常
public class P6InboundExceptionHandler extends ChannelInboundHandlerAdapter {
//    重写exceptionCaught方法
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

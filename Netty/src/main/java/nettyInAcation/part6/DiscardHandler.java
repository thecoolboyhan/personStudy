package nettyInAcation.part6;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

//利用ChannelInboundHandlerAdapter显式的释放内存
@ChannelHandler.Sharable
public class DiscardHandler extends ChannelInboundHandlerAdapter {
//    用来显式的释放与池化的ByteBuf实例相关的内存
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        丢失已接收的消息
        ReferenceCountUtil.release(msg);
    }
}

//利用SimpleChannelInboundHandler显式的释放内存
class SimpleDiscardHandler extends SimpleChannelInboundHandler<Object>{
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
//        自动释放内存，不需要手动
    }
}

package nettyInAcation.part6;

import io.netty.channel.*;

import java.util.concurrent.Future;

//自定义出站规则异常处理
public class P6OutboundExceptionHandler extends ChannelOutboundHandlerAdapter {
//    将结果的监听器交给operationComplete
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        promise.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(!future.isSuccess()){
                    future.cause().printStackTrace();
                    future.channel().close();
                    future.channel().closeFuture();
                }
            }
        });
    }
//    通过将监听器交给Future，两种方法效果相同
    public void byChannelFuture(Channel channel){
        ChannelFuture future = channel.write(new Object());
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(!future.isSuccess()){
                    future.cause().printStackTrace();
                    future.channel().close();
                    future.channel().closeFuture();
                }
            }
        });
    }
}

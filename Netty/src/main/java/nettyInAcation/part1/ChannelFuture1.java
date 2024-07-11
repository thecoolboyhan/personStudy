package nettyInAcation.part1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * 演示利用ChannelFuture处理连接请求，和连接请求的结果。全程不影响其他代码。
 */
public class ChannelFuture1 {
    public void execute(Channel channel){
//        连接会异步的建立，不影响其他逻辑代码。
        ChannelFuture future = channel.connect(new InetSocketAddress("192.168.31.141", 25));
        //给此结果添加一个ChannelFutureListener处理方法，并根据返回结果来做出回应
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(future.isSuccess()){
                    ByteBuf buffer = Unpooled.copiedBuffer("Hello", Charset.defaultCharset());
                    ChannelFuture wf = future.channel().writeAndFlush(buffer);
                }else{
                    Throwable cause = future.cause();
                    cause.printStackTrace();
                }
            }
        });
    }
}

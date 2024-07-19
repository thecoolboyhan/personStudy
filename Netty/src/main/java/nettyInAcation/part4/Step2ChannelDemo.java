package nettyInAcation.part4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.CharsetUtil;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Step2ChannelDemo {
//    写出数据到Channel
    public void demo1(Channel channel){
//        申请空间
        ByteBuf buf = Unpooled.copiedBuffer("your data", CharsetUtil.UTF_8);
//        写完数据并冲刷（提交）
        ChannelFuture cf = channel.writeAndFlush(buf);
//        添加ChannelFutureListener用来写完后接收通知
        cf.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(future.isSuccess())
                    System.out.println("write successful");
                else{
                    System.err.println("write error");
                    future.cause().printStackTrace();
                }
            }
        });
    }
//    多个线程使用同一个Channel
    public void demo2(Channel channel){
        ByteBuf buf = Unpooled.copiedBuffer("your data", CharsetUtil.UTF_8);
//        创建一个把数据写到Channel的runnable
        Runnable write = new Runnable() {
            @Override
            public void run() {
                channel.writeAndFlush(buf.duplicate());
            }
        };
//        获取一个线程池
        Executor executor= Executors.newCachedThreadPool();
//        把任务提交给一个线程
        executor.execute(write);
//        把任务提交给另一个线程
        executor.execute(write);
    }
}

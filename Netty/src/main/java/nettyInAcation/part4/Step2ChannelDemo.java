package nettyInAcation.part4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.CharsetUtil;

import java.util.Collections;
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

    /**
     * 堆缓冲区方式使用bytebuf。
     * @param buf   缓冲区
     */
    public void byteBufNum(ByteBuf buf){
//        检测bytebuf是否存在支撑数组
        if(buf.hasArray()){
//            获取该支撑数组
            byte[] array = buf.array();
//            计算出当前的偏移量
            int offset = buf.arrayOffset() + buf.readerIndex();
//            获取可读的字节数
            int len = buf.readableBytes();
//            handleArray(array,offset,len);
        }
    }

    /**
     * 通过直接内存操作缓冲区
     * @param directBuf 缓冲区
     */
    public  void byteBufDirect(ByteBuf directBuf){
//        检测是否存在数组缓冲区，如果不则认为当前缓冲区是直接内存缓冲区
        if(!directBuf.hasArray()){
//            获取当前缓冲区的可读长度
            int length = directBuf.readableBytes();
//            分配字节数组来接收
            byte[] array = new byte[length];
//            从缓冲区读取数据到上方数组
            directBuf.getBytes(directBuf.readerIndex(),array);
//            handleArray(array,offset,len);
        }
    }

    /**
     * 复合缓冲区
     * @param headByteBuf 头缓冲区
     * @param bodyBuf 主体缓冲区
     */
    public void ByteBufComposite(ByteBuf headByteBuf,ByteBuf bodyBuf){
        CompositeByteBuf messageBuf = Unpooled.compositeBuffer();
        messageBuf.addComponents(headByteBuf,bodyBuf);
//        访问CompositeByteBuf中的数据
        int length = messageBuf.readableBytes();
//        创建一个用来存放复合缓冲区中数据的空间
        byte[] array = new byte[length];
//        将缓冲区中的数据复制到空间中
        messageBuf.getBytes(messageBuf.readerIndex(), array);
//        删除复合缓冲区中的第一个缓冲区
        messageBuf.removeComponent(0);
        for (ByteBuf buf : messageBuf) {
            System.out.println(buf.toString());
        }
    }

}

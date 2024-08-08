package nettyInAcation.part5;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class ByteBufDemo {
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

//    获取ByteBufAllocator对象
    public void ByteBufAllocator(Channel channel, ChannelHandlerContext ctx){
//        从Channel中获取ByteBufAllocator
        ByteBufAllocator allocator = channel.alloc();
//        从ChannelHandler中获取ByteBufAllocator
        ByteBufAllocator allocator2 = ctx.alloc();
    }

//    释放ByteBuf的空间
    public void ByteBufReferenceCounted(Channel channel){
//        从Channel中获取池管理器
        ByteBufAllocator allocator = channel.alloc();
//        从池中获取ByteBuf对象
        ByteBuf buffer = allocator.directBuffer();
//        检查当前ByteBuf引用数是否为1
        assert buffer.refCnt()==1;
//        释放内存
        boolean release = buffer.release();
    }

}

package nettyInAcation.part10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

//继承ByteToMessageDecoder类，创建变成一个新的子解码器
public class ToIntegerDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
//        判断当前缓冲区中数据是否大于等于4字节
        if(in.readableBytes()>=4){
//            超过4字节，读取一个Integer格式的数据
            out.add(in.readInt());
        }
    }
}

package nettyInAcation.part10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

//继承ByteToMessageDecoder类，创建变成一个新的子解码器
public class ToIntegerDecoder2 extends ReplayingDecoder<Void> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        /**
         * 内部的readInt方法增加了校验逻辑
         *     @Override
         *     public int readInt() {
         *         checkReadableBytes(4);
         *         return buffer.readInt();
         *     }
         */
        out.add(in.readInt());
    }
}

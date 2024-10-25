package nettyInAcation.part9;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

//处理入站字节，解码
public class FixedLengthFrameDecoder extends ByteToMessageDecoder {
// 每次读取的长度（每帧的大小）
    private final int frameLength;
    public FixedLengthFrameDecoder(int frameLength) {
        if(frameLength<=0) throw new RuntimeException("大小有误");
        this.frameLength = frameLength;
    }

//    解码
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
//        检查字节是否够一帧
        while (byteBuf.readableBytes()>=frameLength){
//            读取一帧
            ByteBuf buf = byteBuf.readBytes(frameLength);
//            已解码的队列
            list.add(buf);
        }
    }
}

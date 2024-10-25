package nettyInAcation.part9;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;
import static org.junit.Assert.*;

public class FixedLengthFrameDecoderTest {

    @Test
    public void testFramesDecoded(){
//        申请缓存空间，写入9个字节
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();
//        创建一个测试Channel测试FixedLengthFrameDecoder效果，每次写入三个字节
        EmbeddedChannel channel = new EmbeddedChannel(
                new FixedLengthFrameDecoder(3)
        );
//        向测试Channel中写入三个字节的数据
        assertTrue(channel.writeInbound(input.retain()));
//        判断神抽写入成功
        assertTrue(channel.finish());
//        从Channel中读取数据
        ByteBuf read = channel.readInbound();
//       判断写入数据是否相同，下同
        assertEquals(buf.readSlice(3),read);
        read.release();

        read = channel.readInbound();
        assertEquals(buf.readSlice(3),read);
        read.release();

        read = channel.readInbound();
        assertEquals(buf.readSlice(3),read);
        read.release();

        assertNull(channel.readInbound());
        buf.release();
    }

    @Test
    public void testFramesDecoded2(){
//        写入9个字节
        ByteBuf buf = Unpooled.buffer();
        for(int i=0;i<9;i++){
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();
        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));
        assertFalse(channel.writeInbound(input.readBytes(2)));
        assertTrue(channel.writeInbound(input.readBytes(7)));
        assertTrue(channel.finish());
        ByteBuf read = channel.readInbound();
        assertEquals(buf.readSlice(3),read);
        read.release();
        read = channel.readInbound();
        assertEquals(buf.readSlice(3),read);
        read.release();
        read = channel.readInbound();
        assertEquals(buf.readSlice(3),read);
        read.release();
        assertNull(channel.readInbound());
        buf.release();
    }
}

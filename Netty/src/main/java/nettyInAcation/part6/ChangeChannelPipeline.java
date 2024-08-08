package nettyInAcation.part6;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelPipeline;

public class ChangeChannelPipeline {
    public void change(ChannelPipeline pipeline){
        C1FirstHandler c1FirstHandler = new C1FirstHandler();
//        尾插第一个Handler名为handler1
        pipeline.addLast("handler1",c1FirstHandler);
//        头插一个Handler名为handler2
        pipeline.addFirst("handler2",c1FirstHandler);
//        尾插
        pipeline.addLast("handler3",c1FirstHandler);
//        按名字删除Handler
        pipeline.remove("handler3");
//        按照Handler类型来删除
        pipeline.remove(c1FirstHandler);
//        把名为handler2的替换
        pipeline.replace("handler2","handler4",c1FirstHandler);
    }
}

class C1FirstHandler extends ChannelHandlerAdapter{

}

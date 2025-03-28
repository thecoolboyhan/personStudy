package nettyInAcation.part8;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;

import java.net.InetSocketAddress;

public class BootWithChannelInitializer {
    public  void run() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(new NioEventLoopGroup(),new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
//                给Channel绑定多个事件
                .childHandler(new ChannelInitializerImpl());
        ChannelFuture future = bootstrap.bind(new InetSocketAddress(8080));
        future.sync();
    }
    final class ChannelInitializerImpl extends ChannelInitializer<Channel>{
        @Override
        protected void initChannel(Channel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast(new HttpClientCodec());
            pipeline.addLast(new HttpObjectAggregator(Integer.MAX_VALUE));
        }
    }
}

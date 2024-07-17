package nettyInAcation.part4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Netty OIO演示
 */
public class NettyOioServer {
    public void server(int port) throws InterruptedException {
        final ByteBuf buf = Unpooled.unreleasableBuffer(
                Unpooled.copiedBuffer("Hi!\r\t", StandardCharsets.UTF_8));
//        新建一个事件处理Oio的线程组
        EventLoopGroup group = new OioEventLoopGroup();
        try {
//            创建一个启动引导类
            ServerBootstrap b = new ServerBootstrap();
//            把OIO组绑定到当前引导类
            b.group(group)
//                    给事件组指定处理事件的Channel
                    .channel(OioServerSocketChannel.class)
//                    设计当前server的ip
                    .localAddress(new InetSocketAddress(port))
//                    指定Channel的处理方式,所有连接都会调用此方法
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(
//                                    给当前处理链添加一个拦截处理器
                                    new ChannelInboundHandlerAdapter(){
//                                        处理器实现
                                        @Override
                                        public void channelActive(ChannelHandlerContext ctx) {
//                                            把消息写给客户端
                                            ctx.writeAndFlush(buf.duplicate())
                                                    .addListener(
//                                                            写完消息后,触发连接关闭事件
                                                            ChannelFutureListener.CLOSE);
                                        }
                                    });
                        }
                    });
//            绑定服务器来接收连接
            ChannelFuture f = b.bind().sync();
            f.channel().closeFuture().sync();
        }finally {
//            释放所有资源
            group.shutdownGracefully().sync();
        }
    }
}

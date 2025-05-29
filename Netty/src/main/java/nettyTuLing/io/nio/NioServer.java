package nettyTuLing.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NioServer {

    static List<SocketChannel> channelList=new ArrayList<>();
    public static void main(String[] args) throws IOException {
//        Nio的服务channel，类似于BIO的ServerSocket
        try (ServerSocketChannel serverSocket = ServerSocketChannel.open()) {
//            将此ServerSocket绑定到9000端口
            serverSocket.socket().bind(new InetSocketAddress(9000));
//            将channel设置成非阻塞的
            serverSocket.configureBlocking(false);
            System.out.println("服务器启动成功");

            while (true) {
//                NIO的非阻塞操作由操作系统实现的，底层调用了linux的accept函数
                SocketChannel socketChannel = serverSocket.accept();
//                检测到连接
                if (socketChannel != null) {
                    System.out.println("连接成功");
//                    设置soocketChannel也是非阻塞
                    socketChannel.configureBlocking(false);
//                    把当前SocketChannel入队，方便后面轮询处理
                    channelList.add(socketChannel);
                }
                //            处理连接的请求
                Iterator<SocketChannel> iterator = channelList.iterator();
                while (iterator.hasNext()) {
                    SocketChannel sc = iterator.next();
//                    利用直接内存来处理请求
                    ByteBuffer byteBuffer = ByteBuffer.allocate(6);
                    int len = sc.read(byteBuffer);
                    if (len > 0) {
                        System.out.println("接收的消息："+new String(byteBuffer.array()));
                    }else if (len==-1){//如果断开连接，就出队
                        iterator.remove();
                        System.out.println("客户端断开了连接");
                    }
                }
            }
        }
    }
}

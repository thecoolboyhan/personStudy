package nettyTuLing.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NioSelectorServer {
    public static void main(String[] args) throws IOException {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
//            打开系统Selector处理Channel，创建epoll
             Selector selector = Selector.open();) {
            serverSocketChannel.socket().bind(new InetSocketAddress(9000));
            serverSocketChannel.configureBlocking(false);
//            把Channel注册到Selector上，并让Selector选择连接事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("服务器启动成功");
            while (true){
//                阻塞等待需要处理的事件
                selector.select();
//                返回有事件的key
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
//                遍历处理有事件的请求
                while(iterator.hasNext()){
                    SelectionKey key = iterator.next();
//                    如果是连接事件
                    if(key.isAcceptable()){
                        try (ServerSocketChannel server = (ServerSocketChannel) key.channel()) {
                            SocketChannel socketChannel = server.accept();
                            socketChannel.configureBlocking(false);
//                            给当前连接注册读取时间
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            System.out.println("客户端连接成功了");
                        }
//                        如果是读事件
                    }else if(key.isReadable()){
                        try (SocketChannel server = (SocketChannel) key.channel()) {
                            ByteBuffer byteBuffer = ByteBuffer.allocate(6);
                            int len = server.read(byteBuffer);
                            if(len >0){
                                System.out.println("接收的请求:"+new String(byteBuffer.array()));
                            }else if (len == -1){
                                System.out.println("客户端连接断开");
                                server.close();
                            }
                        }
//                        当前事件已经被处理，删除当前数据
                        iterator.remove();
                    }
                }
            }
        }

    }
}

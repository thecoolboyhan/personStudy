package nettyInAcation.part4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * java原生的NIO连接处理
 */
public class PlainNioServer {
    public void serve(int port) throws IOException {
//        创建一个服务器Channel，
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
//        设置当前Channel为非阻塞的
        serverChannel.configureBlocking(false);
//        获取当前Channel的socket对象
        ServerSocket socket = serverChannel.socket();
//        新建一个ip+端口的网络地址
        InetSocketAddress address = new InetSocketAddress(port);
//        把地址绑定到serverSocket对象上
        socket.bind(address);
//        打开selector：开始接收Channel中的事件
        Selector selector = Selector.open();
//        把serverSocket注册到selector上
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        final ByteBuffer msg = ByteBuffer.wrap("Hi!\r\n".getBytes());
        for (; ; ) {
            try {
//                开始接收时间，目前处于阻塞状态
                selector.select();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
//            获取时间的选择key
            Set<SelectionKey> readyKeys = selector.selectedKeys();
//            开始遍历
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
//                开始处理当前上面的key，则删除此key（避免重复处理）
                iterator.remove();
                try {
//                    判断是否就绪（可以被连接）
                    if(key.isAcceptable()){
//                        获取此key的Channel
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
//                        把当前客户端Channel的读写事件监听注册到selector上
                        client.register(selector,SelectionKey.OP_WRITE|
                                SelectionKey.OP_READ,msg.duplicate());
                        System.out.println("accepted connection from"+client);
                    }
//                    如果写事件就绪
                    if(key.isWritable()){
//                        获取当前客户端的Channel
                        SocketChannel client=(SocketChannel) key.channel();
//                        当前通道的缓存
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        while(buffer.hasRemaining()){
//                            把数据写到当前通道
                            if(client.write(buffer)==0){
                                break;
                            }
                        }
//                        关闭连接
                        client.close();
                    }
                } catch (IOException e) {
                    key.cancel();
                    try {
                        key.channel().close();
                    } catch (IOException ex) {
                        System.out.println("关闭失败！");
                        throw new RuntimeException(ex);
                    }
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

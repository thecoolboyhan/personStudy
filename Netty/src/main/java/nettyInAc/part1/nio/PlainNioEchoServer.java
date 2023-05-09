package nettyInAc.part1.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class PlainNioEchoServer {
    public  void serve(int port) throws IOException {

        System.out.println("监听的端口号:"+port);
        //创建一个NIO的serversocket对象
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        //获取当前socketChannel的socket对象
        ServerSocket ss = serverChannel.socket();
        //创建一个当前网络的socket地址
        InetSocketAddress address = new InetSocketAddress(port);
        //把上面的socket对象绑定到地址
        ss.bind(address);
        //把当前channel设置成非阻塞的，这样可以一步调用下面的读和写
        serverChannel.configureBlocking(false);
        //创建NIO的Selector对象
        Selector selector = Selector.open();
        //把上面的selector注册到socketchannel上
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true){
            try {
                //进行一次select操作，会把监听到的时间记录在selector对象的selectedKeys里
                selector.select();
            }catch (IOException ex){
                ex.printStackTrace();
            }
            //取到上面select到的所有key
            Set<SelectionKey> readKeys = selector.selectedKeys();
            //迭代器迭代
            Iterator<SelectionKey> iterator = readKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                //删除需要处理的key
                iterator.remove();
                try {
                    //如果是连接时间
                    if(key.isAcceptable()){
                        //从select里取一个serverSocketChannel对象
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        //用上面的channel来处理此次连接
                        SocketChannel client = server.accept();
                        System.out.println("收到请求："+client);
                        //把此channel设置成异步的，channel去处理操作，不影响主程序运行
                        client.configureBlocking(false);
                        //把此channel注册到selector上，此channel用来处理写和读事件
                        client.register(selector,SelectionKey.OP_WRITE +SelectionKey.OP_READ, ByteBuffer.allocate(100));
                    }
                    //如果是读事件
                    if(key.isReadable()){
                        //取到读的channel
                        SocketChannel client = (SocketChannel) key.channel();
                        //取到接收到的byteBuffer
                        ByteBuffer output = (ByteBuffer) key.attachment();
                        //用channel来读bf里的数据
                        client.read(output);
                    }
                    //如果是写事件
                    if(key.isWritable()){
                        //取到channel
                        SocketChannel client = (SocketChannel) key.channel();
                        //取到接收到的byteBuffer
                        ByteBuffer output = (ByteBuffer) key.attachment();
                        //把bf切换到写模式
                        output.flip();
                        //用channel来写入bf中的内容
                        client.write(output);
                        //把position调整到当前byteBuffer之后的位置，不影响下次读取和写入
                        output.compact();
                    }
                }catch (IOException e){
                    //取消channel
                    key.cancel();
                    try {
                        //关闭channel
                        key.channel().close();
                    }catch (IOException exception){
                    }
                }
            }
        }
    }
}

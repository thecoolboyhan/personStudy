package com.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * 韩永发
 *
 * @author hp
 * @Date 16:45 2022/4/14
 */
public class NIOServer {
  public static void main(String[] args) throws IOException, InterruptedException {
    //1.打开一个服务端通道
    ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
    //2.绑定对应的端口号
    serverSocketChannel.bind(new InetSocketAddress(9999));
    //3.通道默认是阻塞的。需要设置为非阻塞的
    serverSocketChannel.configureBlocking(false);
    System.out.println("服务端启动成功");
    while (true){
      //4.检查是否有客户端连接，有客户端连接会返回对应的通道
      SocketChannel socketChannel = serverSocketChannel.accept();
      if (socketChannel==null){
        System.out.println("做其他事情......");
        Thread.sleep(5000);
        continue;
      }
      //5获取客户端传来的数据，并把数据放入byteBuffer缓冲区中
      ByteBuffer allocate = ByteBuffer.allocate(1024);
      int read = socketChannel.read(allocate);
      System.out.println("客户端的消息"+new String(allocate.array(), 0, read, StandardCharsets.UTF_8));
      //6给客户端返回数据
      socketChannel.write(ByteBuffer.wrap("没钱".getBytes(StandardCharsets.UTF_8)));
      //7.释放资源
      socketChannel.close();
    }

  }
}

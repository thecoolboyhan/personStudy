package nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * 韩永发
 * 服务端Selector选择器
 *
 * @author hp
 * @Date 15:51 2022/4/18
 */
public class NIOSelectorServer {
  public static void main(String[] args) throws IOException {
    //1.打开一个服务器通道
    ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
    //2。绑定对应的端口号
    serverSocketChannel.bind(new InetSocketAddress(9999));
    //3,通道默认是阻塞的，需要设置为非阻塞
    serverSocketChannel.configureBlocking(false);
    //4.创建选择器
    Selector selector = Selector.open();
    //5.将服务器通道注册到选择器上，并指定注册监听的事件为OP_ACCEPT
    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    System.out.println("服务端启动成功!");
    while (true) {
      //6.检查选择器是否有事件
      int select = selector.select(2000);
      if (select == 0) {
        System.out.println("没有事情发生。。。。");
        continue;
      }
      //7.获取事件集合
      Set<SelectionKey> selectionKeys = selector.selectedKeys();
      Iterator<SelectionKey> iterator = selectionKeys.iterator();
      while (iterator.hasNext()) {
        //8.判断事件是否为客户端连接事件
        SelectionKey selectionKey = iterator.next();
        if (selectionKey.isAcceptable()) {
          //9.得到客户端通道，并将通道注册到选择器上，并指定监听事件为OP_READ
          SocketChannel socketChannel = serverSocketChannel.accept();
          System.out.println("有客户端连接。。。。");
          //将通道设置为非阻塞状态，因为选择器要轮询监听每个通道
          socketChannel.configureBlocking(false);
          //指定监听器事件为OP_READ读就绪事件
          socketChannel.register(selector, SelectionKey.OP_READ);
        }
        //10.判断是否是客户端读就绪事件SelectionKey.isReadable
        if (selectionKey.isReadable()) {
          //11.得到客户端通道，读取数据到缓冲区中
          SocketChannel channel = (SocketChannel) selectionKey.channel();
          ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
          int read = channel.read(byteBuffer);
          if (read > 0) {
            System.out.println("客户端消息：" + new String(byteBuffer.array(), 0, read, StandardCharsets.UTF_8));
            //12.给客户端回写数据
            channel.write(ByteBuffer.wrap("没钱".getBytes(StandardCharsets.UTF_8)));
            channel.close();
          }
        }
        //13.从集合中删除对应事件
        iterator.remove();
      }

    }
  }

}

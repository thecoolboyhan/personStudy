package nettyTuLing.io.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//所有的操作都是阻塞的
public class SocketServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9000);
        while(true){
            System.out.println("等待连接");
//            阻塞的
            Socket clientSocket = serverSocket.accept();
            System.out.println("有客户端连接了。。");
//            在单线程上做优化，每接收的一个连接，就开启一个新的线程来处理
            Thread.ofVirtual().start(()->{
                try {
                    handler(clientSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private static void handler(Socket clientSocket) throws IOException {
        byte[] bytes = new byte[1024];
        System.out.println("准备read。。。");
//        接收客户端的数据，阻塞方法，没有数据可读时就阻塞
        int read = clientSocket.getInputStream().read(bytes);
        System.out.println("read完毕");
        if(read!=-1){
            System.out.println("接收到客户端的数据："+new String(bytes,0,read));
        }
        System.out.println("end");
    }
}

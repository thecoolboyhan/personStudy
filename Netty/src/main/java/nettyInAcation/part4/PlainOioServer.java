package nettyInAcation.part4;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * java原生的OIO连接网络处理
 */
public class PlainOioServer {
    public void serve(int port) throws IOException{
//        给服务器绑定指定端口
        final ServerSocket socket=new ServerSocket(port);
        try {
            for (;;){
//                接收连接
                final Socket clientSocket=socket.accept();
                System.out.println("Accepted conection from "+clientSocket);
//                创建一个线程处理连接
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OutputStream out;
                        try {
//                            给连接自己的客户端返回消息
                            out=clientSocket.getOutputStream();
                            out.write("Hi!\r\n".getBytes(
                                    StandardCharsets.UTF_8));
                            out.flush();
//                            关闭连接
                            clientSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        finally{
                            try {
                                clientSocket.close();
                            } catch (IOException e) {
//                                关闭异常
                            }
                        }
                    }
//                    线程启动
                }).start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

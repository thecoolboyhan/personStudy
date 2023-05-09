package nettyInAc.part1.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 基于bio的实现
 */
public class EchoServer {

    public void serve(int port) throws IOException{
        //利用socket创建一个ServerSocket
        final ServerSocket socket = new ServerSocket(port);
        try {
            while (true){
                //当ServerSocket接收到连接
                final Socket clientSocket = socket.accept();
                System.out.println("Accepted connection from" + clientSocket);
                //新建一个线程来处理连接请求
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //利用输入输出流来处理数据的数据
                            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                            writer.println(reader.readLine());
                            writer.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                            try {
                                clientSocket.close();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }

                    }
                }).start();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

package Concurrency.VirtualThreads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
    public static void main(String[] args) {
//        if(args.length != 1){
//            System.out.println("usage: java EchoServer <port>");
//            System.exit(0);
//        }
        int port = 8080;
//        传入端口号
//        int port = Integer.parseInt(args[0]);
        try(
                ServerSocket serverSocket = new ServerSocket(port)
        ){
            while(true){
//                不知道hostname
//                System.out.println(serverSocket.getInetAddress().getHostName());
//                获取到连接请求，创建一个虚拟线程来处理
                Socket clientSocket = serverSocket.accept();
//                创建虚拟线程的方式为Thread类
                Thread.ofVirtual().start(()->{
                   try(
//                           输入输出流
                           PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
                           BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
                   ) {
//                       获取客户端发送来的请求
                       String inputLine;
                       while((inputLine=in.readLine())!=null){
                           System.out.println(inputLine);
                           out.println(inputLine);
                       }
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
                });
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

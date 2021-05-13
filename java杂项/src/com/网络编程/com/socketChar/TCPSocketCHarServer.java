package com.网络编程.com.socketChar;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPSocketCHarServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9999);
        Socket accept = serverSocket.accept();
        InputStream inputStream = accept.getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String s = bufferedReader.readLine();
        System.out.println(s);
        //给客户端发送数据
        OutputStream outputStream = accept.getOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        bufferedWriter.write("hello,client");
        //结束输出标识
        bufferedWriter.newLine();
        bufferedWriter.flush();

        //关闭最外层流
        bufferedWriter.close();
        bufferedReader.close();
        accept.close();
        serverSocket.close();
        System.out.println("服务器关闭");
    }
}

package com.网络编程.com.socketTest;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9999);
        Socket accept = serverSocket.accept();
        InputStream inputStream = accept.getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String s = bufferedReader.readLine();
        String answer="";
        System.out.println(s);
        if ("name".equals(s)){
            answer="你想知道我的名字？";
        }else if ("hobby".equals(s)){
            answer="写代码";
        }else {
            answer="你输入了什么东西？";
        }

        //给客户端发送数据
        OutputStream outputStream = accept.getOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        bufferedWriter.write(answer);
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

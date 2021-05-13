package com.网络编程.com.socketTest;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(InetAddress.getLocalHost(), 9999);
        OutputStream outputStream = socket.getOutputStream();
        //把字节流转换为字符流
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);

        //从键盘读取用户的问题
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入你的问题");
        String next = scanner.next();

        //把接收到的问题发给服务器
        bufferedWriter.write(next);
        //插入换行符，表示写入结束，对方必须使用readLine的方式来读
        bufferedWriter.newLine();
        //字符流必须手动刷新，否则数据不会写入通道
        bufferedWriter.flush();
        socket.shutdownOutput();


        //接收
        InputStream inputStream = socket.getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String s = bufferedReader.readLine();
        System.out.println(s);

        //关闭最外层流
        bufferedReader.close();
        bufferedWriter.close();
        socket.close();
        System.out.println("客户端关闭");
    }
}

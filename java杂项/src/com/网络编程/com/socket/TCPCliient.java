package com.网络编程.com.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPCliient {

    public static void main(String[] args) throws IOException {
        //连接服务器(ip,端口)
        //创建需要连接的socket对象，如果连接成功返回socket对象
        Socket socket = new Socket(InetAddress.getLocalHost(), 9999);
        System.out.println("客户端socket="+socket.getClass());
        //得到socket对象的输出流
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("hello,server".getBytes());
        //设置一个out结束标记
        socket.shutdownOutput();
        //获取返回
        InputStream inputStream = socket.getInputStream();
        byte[] bytes = new byte[1024];
        int byteLen =0;
        while ((byteLen = inputStream.read(bytes))!=-1){
            System.out.println(new String(bytes,0,byteLen));
        }

        //关闭流对象和socket
        outputStream.close();
        socket.close();
        System.out.println("客户端退出");
    }
}

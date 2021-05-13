package com.网络编程.com.socket;

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 字节流网络通信
 */
public class TCPServer {
    public static void main(String[] args) throws IOException {
        //1.监听9999端口，等待连接
        ServerSocket serverSocket = new ServerSocket(9999);
        System.out.println("程序正在监听9999端口");
        //2.如果有程序连接到9999端口，程序进入阻塞状态，返回socket对象
        Socket accept = serverSocket.accept();
        System.out. println("服务器socket = "+ accept.getClass());
        //拿到socket的输入流
        InputStream inputStream = accept.getInputStream();
        byte[] bytes = new byte[1024];
        int readLen =0;
        while ((readLen=(inputStream.read(bytes)))!=-1){
            //根据读到的数据长度来显示内容
            System.out.println(new String(bytes,0,readLen));
        }
        OutputStream outputStream = accept.getOutputStream();
        outputStream.write("hello,CLient".getBytes());
        //设置结束标记
        accept.shutdownOutput();

        //关闭
        outputStream.close();
        inputStream.close();
        accept.close();
        serverSocket.close();
        System.out.println("服务器停止");
    }
}

package com.网络编程.com.上传图片;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTest {
    public static void main(String[] args) throws IOException {
        //监听本机8888
        ServerSocket serverSocket = new ServerSocket(8888);
        System.out.println("开始监听");
        Socket accept = serverSocket.accept();

        //读取客户端发来的数据
        InputStream inputStream = accept.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        byte[] bytes = StreamUtil.streamToByteArray(bis);
        //把字节数组写入指定路径
        String path="java杂项\\src\\qq.jpg";
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path));
        bos.write(bytes);
        bos.close();

        //向客户端回复收到了图片
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(accept.getOutputStream()));
        writer.write("收到图片");
        //把内容刷新到数据通道
        writer.flush();
        accept.shutdownOutput();
        writer.close();

        //关闭其他流
        bis.close();
        accept.close();
        serverSocket.close();

    }
}

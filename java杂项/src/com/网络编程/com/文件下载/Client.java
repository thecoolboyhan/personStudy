package com.网络编程.com.文件下载;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(InetAddress.getLocalHost(), 8888);
        OutputStream outputStream = socket.getOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        BufferedWriter writer1 = new BufferedWriter(writer);
        String aa="下载图片";
        writer1.write(aa);
        writer1.flush();
        writer1.close();
        outputStream.close();
//        socket.shutdownOutput();

        InputStream inputStream = socket.getInputStream();
        byte[] bytes = new byte[1024];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int len;
        while ((len=inputStream.read(bytes))!=-1){
            byteArrayOutputStream.write(bytes,0,len);
        }
        byte[] bytes1 = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        inputStream.close();

        String path="java杂项/src/aa.png";
        FileOutputStream fileOutputStream = new FileOutputStream(path);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        bufferedOutputStream.write(bytes1);
        bufferedOutputStream.close();
        socket.shutdownInput();

        OutputStream outputStream1 = socket.getOutputStream();
        outputStream1.write("收到了图片".getBytes());
        outputStream.close();
        socket.shutdownOutput();

        socket.close();

    }
}

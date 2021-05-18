package com.网络编程.com.文件下载;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(InetAddress.getLocalHost(), 9999);
        OutputStream outputStream = socket.getOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        BufferedWriter writer1 = new BufferedWriter(writer);
        String aa="下载图片22";
        writer1.write(aa);
        writer1.newLine();
        writer1.flush();


        InputStream inputStream = socket.getInputStream();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        byte[] bytes = new byte[1024];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int len;
        while ((len=bufferedInputStream.read(bytes))!=-1){
            byteArrayOutputStream.write(bytes,0,len);
        }
        byte[] bytes1 = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();

        String path="java杂项/src/aa.png";
        FileOutputStream fileOutputStream = new FileOutputStream(path);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        bufferedOutputStream.write(bytes1);
        bufferedOutputStream.close();

//        OutputStream outputStream1 = socket.getOutputStream();
//        outputStream1.write("收到了图片".getBytes());
//        socket.shutdownOutput();
//        outputStream1.close();
        writer1.close();

        outputStream.close();
        inputStream.close();

        socket.close();

    }
}

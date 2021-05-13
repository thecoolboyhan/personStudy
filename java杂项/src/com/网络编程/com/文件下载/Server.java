package com.网络编程.com.文件下载;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8888);
        Socket accept = serverSocket.accept();
        InputStream inputStream = accept.getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String s = bufferedReader.readLine();
        System.out.println("客户端发来的请求----"+s);
        String path="C:\\work\\11.jpg";
        //如果口令正确就给他这个图
        if ("下载图片".equals(s)){
            path="c:\\aaa.png";
        }
        FileInputStream fileInputStream = new FileInputStream(path);
        BufferedInputStream inputStream1 = new BufferedInputStream(fileInputStream);
        byte[] bytes = new byte[1024];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int len;
        while ((len=inputStream1.read(bytes))!=-1){
            byteArrayOutputStream.write(bytes,0,len);
        }
        byte[] bytes1 = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        inputStream1.close();

        //发送给客户端
        OutputStream outputStream = accept.getOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
        bufferedOutputStream.write(bytes1);
        bufferedOutputStream.close();
//        accept.shutdownOutput();

        //接收客户的返回
        InputStream inputStream2 = accept.getInputStream();
        byte[] bytes2 = new byte[1024];
        int len1;
        while ((len1=inputStream2.read(bytes2))!=-1){
            System.out.println(new String(bytes2,0,len1));
        }
        accept.shutdownInput();
        inputStream2.close();
        accept.close();
        serverSocket.close();

    }
}

package com.网络编程.com.上传图片;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientTest {
    public static void main(String[] args) throws IOException {
        //
        Socket socket = new Socket(InetAddress.getLocalHost(), 8888);
        //从磁盘读取文件
        String path="C:\\work\\11.jpg";
        FileInputStream fileInputStream = new FileInputStream(path);
        BufferedInputStream bis = new BufferedInputStream(fileInputStream);
        byte[] bytes = StreamUtil.streamToByteArray(bis);
        //把数组发过去
        OutputStream outputStream = socket.getOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(outputStream);
        bos.write(bytes);
        bis.close();
        //设置结束标志
        socket.shutdownOutput();

        //接收服务端黑腹的消息
        InputStream inputStream = socket.getInputStream();
        String s = StreamUtil.streamToString(inputStream);
        System.out.println(s);

        //关闭涉及的流
        bos.close();
        socket.close();
    }
}

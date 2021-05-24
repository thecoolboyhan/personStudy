package com.多用户及时通讯系统.客户端.servive;

import com.多用户及时通讯系统.common.Message;
import com.多用户及时通讯系统.common.MessageType;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

//客户端给服务器发送消息管理
public class ClientConnectServerThread extends Thread {
    private Socket socket;

    public ClientConnectServerThread(Socket socket){
        this.socket=socket;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {
        while (true){
            System.out.println("客户端的线程等待读取服务器发送的消息");
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                //如果服务器没有发送message，线程会阻塞在这
                Message message = (Message)ois.readObject();
                //根据message的；类型做不同的操作
                if (message.getMsgType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)){
                    String[] users = message.getContent().split("-");
                    for (String user : users) {
                        System.out.println("用户：" + user);
                    }
                    //普通聊天
                }else if (message.getMsgType().equals(MessageType.MESSAGE_COMM_MES)){
                    System.out.println("\n"+message.getSender()+"对"+message.getGetter()+"说"+message.getContent());
                    System.out.println("\t\t时间："+message.getSendTime());
                    //接收群消息
                }else if (message.getMsgType().equals(MessageType.MESSAGE_TO_ALL_MES)){
                    System.out.println(message.getSender()+"对大家说"+message.getContent());
                    //接收文件
                }else if (message.getMsgType().equals(MessageType.MESSAGE_FILE_MES)){
                    System.out.println("接收"+message.getSender()+"给"+message.getGetter()+"文件"+message.getSrc()+"到我的电脑"+message.getDest()+"目录");

                    //把文件写入磁盘
                    FileOutputStream fileOutputStream = new FileOutputStream(message.getDest());
                    fileOutputStream.write(message.getFileBytes());
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    System.out.println("保存成功");

                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}

package com.多用户及时通讯系统.客户端.servive;

import com.多用户及时通讯系统.common.Message;
import com.多用户及时通讯系统.common.MessageType;

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
                if (message.getMsgType().equals(MessageType.MESSAGE_RET_ONLINE_FRUEND)){
                    String[] users = message.getContent().split("-");
                    for (String user : users) {
                        System.out.println("用户：" + user);
                    }

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

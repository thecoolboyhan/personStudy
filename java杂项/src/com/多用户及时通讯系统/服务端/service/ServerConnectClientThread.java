package com.多用户及时通讯系统.服务端.service;

import com.多用户及时通讯系统.common.Message;
import com.多用户及时通讯系统.common.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

//服务器
public class ServerConnectClientThread extends Thread {
    private Socket socket;
    private String userId;

    public ServerConnectClientThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public void run() {
        while (true){
            System.out.println("服务端读取"+userId+"客户端的数据");
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message)ois.readObject();
                if (message.getMsgType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)){
                    //客户端请求在线用户列表
                    System.out.println(message.getSender()+"请求获取在线列表");
                    String onlineUser = ManageClientThread.getOnlineUser();
                    Message message1 = new Message();
                    message1.setMsgType(MessageType.MESSAGE_RET_ONLINE_FRUEND);
                    message1.setContent(onlineUser);
                    message1.setGetter(message.getSender());
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message1);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}

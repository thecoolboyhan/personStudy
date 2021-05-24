package com.多用户及时通讯系统.服务端.service;

import com.多用户及时通讯系统.common.Message;
import com.多用户及时通讯系统.common.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

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
                //在线列表
                if (message.getMsgType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)){
                    //客户端请求在线用户列表
                    System.out.println(message.getSender()+"请求获取在线列表");
                    String onlineUser = ManageClientThread.getOnlineUser();
                    Message message1 = new Message();
                    message1.setMsgType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    message1.setContent(onlineUser);
                    message1.setGetter(message.getSender());
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message1);
                    //关闭
                }else if (message.getMsgType().equals(MessageType.MESSAGE_CLIENT_EXIT)){
                    System.out.println(message.getSender()+"要退出系统");
                    //将客户端对应的线程从集合中删除
                    ManageClientThread.removeByUserId(userId);
                    socket.close();
                    System.out.println(userId+"退出了系统");
                    //退出循环
                    break;
                    //私聊
                }else if (message.getMsgType().equals(MessageType.MESSAGE_COMM_MES)){
                    ObjectOutputStream oos = new ObjectOutputStream(ManageClientThread.getThreadByUserId(message.getGetter()).getSocket().getOutputStream());
                    oos.writeObject(message);
                    //如果不在线可以保存到数据库,可以实现离线留言
                } else if (message.getMsgType().equals(MessageType.MESSAGE_TO_ALL_MES)) {
                    HashMap<String, ServerConnectClientThread> hashMap = ManageClientThread.getHashMap();
                    Set<String> strings = hashMap.keySet();
                    Iterator<String> iterator = strings.iterator();
                    while (iterator.hasNext()){
                        //去除所有在线的id
                        String userId = iterator.next().toString();
                        if (userId.equals(message.getSender())){
                            continue;
                        }
                        ObjectOutputStream oos = new ObjectOutputStream(hashMap.get(userId).getSocket().getOutputStream());
                        oos.writeObject(message);
                    }
                    //文件类型
                }else if(message.getMsgType().equals(MessageType.MESSAGE_FILE_MES)){
                    ObjectOutputStream oos = new ObjectOutputStream(ManageClientThread.getThreadByUserId(message.getGetter()).getSocket().getOutputStream());
                    oos.writeObject(message);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}

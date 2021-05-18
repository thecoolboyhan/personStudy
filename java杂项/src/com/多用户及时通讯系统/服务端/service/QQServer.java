package com.多用户及时通讯系统.服务端.service;

import com.多用户及时通讯系统.common.Message;
import com.多用户及时通讯系统.common.MessageType;
import com.多用户及时通讯系统.common.User;
import com.多用户及时通讯系统.客户端.servive.ManageClientServerThread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class QQServer {
    private ServerSocket serverSocket;

    private static HashMap<String,User> validUsers=new HashMap<>();

    //用来存放账号密码
    static {
        validUsers.put("100",new User("100","123456"));
        validUsers.put("200",new User("200","123456"));
        validUsers.put("300",new User("300","123456"));
        validUsers.put("400",new User("400","123456"));
        validUsers.put("500",new User("500","123456"));
    }

    public QQServer(){
        System.out.println("服务端在9999端口监听。。。");
        try {
            serverSocket=new ServerSocket(9999);
            while (true) {//
                Socket socket = serverSocket.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                User user = (User)ois.readObject();
                Message message = new Message();
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                //判断账号密码是否正确
                if (checkUser(user.getUserId(),user.getPasswd())){
                    //把message回复给客户端
                    message.setMsgType(MessageType.MESSAGE_LOGIN_SUCCEED);
                    oos.writeObject(message);
                    //
                    //i创建一个线程和客户端保持通讯
                    ServerConnectClientThread serverConnectClientThread = new ServerConnectClientThread(socket, user.getUserId());
                    //启动这个线程，读取数据
                    serverConnectClientThread.start();
                    ManageClientThread.addClientThread(user.getUserId(),serverConnectClientThread);
                }else {
                    //登录失败
                    System.out.println(user.getUserId()+user.getPasswd()+"登录失败");
                    message.setMsgType(MessageType.MESSAGE_LOGIN_FAIL);
                    oos.writeObject(message);
                    socket.close();
                }

            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            //服务端退出了while循环，关闭服务器资源
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 账号密码验证
     * @param userId
     * @param pwc
     * @return
     */
    private boolean checkUser(String userId,String pwc){
        User user = validUsers.get(userId);
        if (user==null){
            return false;
        }
        if (pwc.equals(user.getPasswd())){
            return true;
        }
        return false;
    }



}

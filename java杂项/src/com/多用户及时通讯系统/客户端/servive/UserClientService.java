package com.多用户及时通讯系统.客户端.servive;

import com.多用户及时通讯系统.common.Message;
import com.多用户及时通讯系统.common.MessageType;
import com.多用户及时通讯系统.common.User;
import com.多用户及时通讯系统.服务端.service.ManageClientThread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

//客户端
public class UserClientService {
    private User user=new User();
    private Socket  socket;
    public boolean checkUser(String userId,String pwd){
        boolean flag=false;
        user.setUserId(userId);
        user.setPasswd(pwd);
        //把user对象发送到服务器
        try {
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999);
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(user);

            //读取服务器的返回
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message object = (Message)ois.readObject();
            if (object.getMsgType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)){
                //创建一个和服务器端保持通讯的线程
                ClientConnectServerThread clientConnectServerThread = new ClientConnectServerThread(socket);
                //一直从服务器读取数据
                 clientConnectServerThread.start();
                 //把当前线程放入管理类中
                ManageClientServerThread.addClientServerThread(userId,clientConnectServerThread);
                 flag=true;
            }else {
                //失败,就关闭socket
                socket.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }

    /**
     * 请求获取当前在线用户列表
     */
    public void getOnlineFriendList(){
        Message message = new Message();
        message.setSender(user.getUserId());
        message.setMsgType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        try {
            //得到当前线程socket的输出流
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientServerThread.GetThreadByUserId(user.getUserId()).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void closeClient(){
        Message message = new Message();
        message.setSender(user.getUserId());
        message.setMsgType(MessageType.MESSAGE_CLIENT_EXIT);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientServerThread.GetThreadByUserId(user.getUserId()).getSocket().getOutputStream());
            oos.writeObject(message);
            System.out.println(user.getUserId()+"退出了系统");
            System.exit(0);//结束进程

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

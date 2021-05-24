package com.多用户及时通讯系统.客户端.servive;

import com.多用户及时通讯系统.common.Message;
import com.多用户及时通讯系统.common.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class MessageClientService {

    //群发
    public void sendMessageToAll(String content,String senderId){
        Message message = new Message();
        message.setMsgType(MessageType.MESSAGE_TO_ALL_MES);
        message.setContent(content);
        message.setSender(senderId);
        message.setSendTime(String.valueOf(System.currentTimeMillis()/1000));
        System.out.println(senderId+"对所有人说"+content);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientServerThread.GetThreadByUserId(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //私聊
    public void sendMessageToOne(String content,String senderId,String getterId){
        Message message = new Message();
        message.setMsgType(MessageType.MESSAGE_COMM_MES);
        message.setContent(content);
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setSendTime(String.valueOf(System.currentTimeMillis()/1000));
        System.out.println(senderId+"对"+getterId+"说"+content);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientServerThread.GetThreadByUserId(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

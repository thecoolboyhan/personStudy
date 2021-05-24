package com.多用户及时通讯系统.客户端.servive;

import com.多用户及时通讯系统.common.Message;
import com.多用户及时通讯系统.common.MessageType;
import com.多用户及时通讯系统.客户端.util.Utility;
import com.多用户及时通讯系统.服务端.service.ManageClientThread;
import com.多用户及时通讯系统.服务端.service.ServerConnectClientThread;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


public class SendNewsToAllService implements Runnable {

    @Override
    public void run() {

        while (true) {
            System.out.println("请输入服务器要推送的消息(输入exit退出推送线程，，，)");
            String news = Utility.readString(100);
            if ("exit".equals(news)){
                break;
            }
            //群发
            Message message = new Message();
            message.setSender("服务器");
            message.setContent(news);
            message.setMsgType(MessageType.MESSAGE_TO_ALL_MES);
            message.setSendTime(String.valueOf(System.currentTimeMillis()));
            HashMap<String, ServerConnectClientThread> hashMap = ManageClientThread.getHashMap();
            Set<String> strings = hashMap.keySet();
            Iterator<String> iterator = strings.iterator();
            while (iterator.hasNext()) {
                String next = iterator.next();
                ServerConnectClientThread serverConnectClientThread = hashMap.get(next);
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                    oos.writeObject(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}

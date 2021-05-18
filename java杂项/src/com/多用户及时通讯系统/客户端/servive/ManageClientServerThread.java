package com.多用户及时通讯系统.客户端.servive;

import java.util.HashMap;
import java.util.Iterator;

//管理客户端连接到服务器端的线程
public class ManageClientServerThread {
    //用来放线程，key用用户id
    private static HashMap<String,ClientConnectServerThread> hashMap=new HashMap<>();

    //把线程加入到集合中
    public static void addClientServerThread(String userId,ClientConnectServerThread clientConnectServerThread){
        hashMap.put(userId,clientConnectServerThread);
    }

    //通过userId得到线程
    public static ClientConnectServerThread GetThreadByUserId(String userId){
        ClientConnectServerThread thread = hashMap.get(userId);
        return thread;
    }


}

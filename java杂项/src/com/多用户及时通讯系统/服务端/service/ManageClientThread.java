package com.多用户及时通讯系统.服务端.service;

import java.util.HashMap;
import java.util.Iterator;

//服务端
public class ManageClientThread {
    private static HashMap<String,ServerConnectClientThread> hashMap=new HashMap<>();

    public static HashMap<String,ServerConnectClientThread> getHashMap(){
        return hashMap;
    }

    //添加线程对象
    public static void addClientThread(String userId,ServerConnectClientThread serverConnectClientThread){
        hashMap.put(userId,serverConnectClientThread);
    }

    //根据userid放回线程
    public static ServerConnectClientThread getThreadByUserId(String userId){
         return hashMap.get(userId);
    }

    //从集合中移除摸个线程
    public static void removeByUserId(String userId){
        hashMap.remove(userId);
    }

    //返回在线用户列表
    public static String getOnlineUser(){
        Iterator<String> iterator = hashMap.keySet().iterator();
        StringBuffer onLineUser = new StringBuffer();
        while (iterator.hasNext()){
            onLineUser.append(iterator.next()).append("-");
        }
        return onLineUser.toString();
    }

}

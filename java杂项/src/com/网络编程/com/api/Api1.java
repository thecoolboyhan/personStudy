package com.网络编程.com.api;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Api1 {
    public static void main(String[] args) throws UnknownHostException {

        //获取本机的InetAddress对象
        InetAddress localHost = InetAddress.getLocalHost();
        System.out.println(localHost);


        //根据主机名来获取InetAddress对象
        InetAddress byName = InetAddress.getByName("DESKTOP-VM71EV1");
        System.out.println(byName);

        //根据域名获取InetAddress对象
        InetAddress byName1 = InetAddress.getByName("www.baidu.com");
        System.out.println(byName1);

        //通过InetAddress对象获取主机ip地址
        String hostAddress = byName1.getHostAddress();
        System.out.println(hostAddress);


    }
}

package com.多用户及时通讯系统.客户端.view;

import com.多用户及时通讯系统.客户端.servive.UserClientService;
import com.多用户及时通讯系统.客户端.util.Utility;

//客户端菜单
public class QQView {

    private boolean loop=true;//是否显示菜单
    private String key="";
    private UserClientService userClientService=new UserClientService();


    public static void main(String[] args) {
        QQView qqView = new QQView();
        qqView.mainMenu();
        System.out.println("客户端退出系统");
    }


    //显示主菜单
    private void mainMenu(){

        while (loop) {
            System.out.println("============欢迎登录网络通讯系统================");
            System.out.println("\t\t1。登录系统");
            System.out.println("\t\t9。退出系统");

            key= Utility.readString(1);
            switch (key){
                case "1":
                    System.out.printf("请输入用户号：");
                    String userId = Utility.readString(50);
                    System.out.printf("请输入密码：");
                    String pwd = Utility.readString(50);
                    //验证是否合法
                    if (userClientService.checkUser(userId,pwd)){
                        //进入二级菜单
                        System.out.println("============欢迎"+userId+"================");

                        while (loop){
                            System.out.println("============二级菜单"+userId+"==============");
                            System.out.println("\t\t1。显示在线用户列表");
                            System.out.println("\t\t2.群发消息");
                            System.out.println("\t\t3.私聊");
                            System.out.println("\t\t4.发送文件");
                            System.out.println("\t\t9.退出系统");
                            System.out.printf("请输入你的选择:");
                            key = Utility.readString(1);
                            switch (key){
                                case "1":
                                    System.out.println("\t\t显示在线用户列表");
                                    userClientService.getOnlineFriendList();
                                    break;
                                case "2":
                                    System.out.println("\t\t群发消息");
                                    break;
                                case "3":
                                    System.out.println("\t\t3私聊");
                                    break;
                                case "4":
                                    System.out.println("\t\t发送文件");
                                    break;
                                case "9":
                                    loop=false;
                                    break;

                            }
                        }
                    }else {
                        System.out.println("登录失败");
                    }
                    break;
                case "9":
                    loop=false;
                    break;
                default:
                    break;
            }

        }
    }
}

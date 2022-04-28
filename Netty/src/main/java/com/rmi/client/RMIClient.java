package com.rmi.client;

import com.rmi.pojo.User;
import com.rmi.service.UserService;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * 韩永发
 *
 * @Date 10:59 2022/4/26
 */
public class RMIClient {
  public static void main(String[] args) throws RemoteException, NotBoundException {
    //1.获取Registry的实例
    Registry registry = LocateRegistry.getRegistry("127.0.0.1", 9998);
    //2.通过Registry实例查找远程对象
    UserService userS = (UserService) registry.lookup("userS");
    User userById = userS.getUserById(2);
    System.out.println(userById.getId()+"-------"+userById.getName());
  }
}

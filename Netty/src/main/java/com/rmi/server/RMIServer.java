package com.rmi.server;

import com.rmi.service.UserService;
import com.rmi.service.UserServiceImpl;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * 韩永发
 *
 * @Date 10:46 2022/4/26
 */
public class RMIServer {
  public static void main(String[] args) {
    try {
      //1.注册Registry实例，绑定端口
      Registry registry = LocateRegistry.createRegistry(9998);
      //2,创建远程对象
      UserService userService = new UserServiceImpl();
      //3.将远程对象注册到RMI服务器上（即服务端注册表上）
      registry.rebind("userS",userService);
    } catch (RemoteException e) {
      e.printStackTrace();
    }

  }
}

package com.rmi.service;

import com.rmi.pojo.User;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

/**
 * 韩永发
 *
 * @Date 10:43 2022/4/26
 */
public class UserServiceImpl extends UnicastRemoteObject implements UserService{
  Map<Object,User> userMap =new HashMap();

  public UserServiceImpl() throws RemoteException {
    super();
    User user1 = new User();
    user1.setId(1);
    user1.setName("张三");
    User user2 = new User();
    user2.setId(2);
    user2.setName("李四");
    userMap.put(user1.getId(),user1);
    userMap.put(user2.getId(),user2);
  }

  @Override
  public User getUserById(int id) throws RemoteException {
    return userMap.get(id);
  }
}

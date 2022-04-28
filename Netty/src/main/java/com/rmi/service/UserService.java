package com.rmi.service;

import com.rmi.pojo.User;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * 韩永发
 *
 * @author hp
 * @Date 10:42 2022/4/26
 */
public interface UserService extends Remote {
  public User getUserById(int id) throws RemoteException;
}

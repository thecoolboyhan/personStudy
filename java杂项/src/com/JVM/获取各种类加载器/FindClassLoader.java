package com.JVM.获取各种类加载器;

import java.sql.DriverManager;

/**
 * 韩永发
 *
 * @Date 19:47 2021/4/7
 */
public class FindClassLoader {

  public static void main(String[] args) {
    //几种获取当前类的ClassLoader的方式
    try {
      //1.
      Class<?> aClass = Class.forName("com.JVM.获取各种类加载器.FindClassLoader");
      ClassLoader classLoader = aClass.getClassLoader();
      //2.
      ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
      //3
      ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();

    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
}

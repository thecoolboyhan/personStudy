package com.JVM.获取各种类加载器;

import java.util.Calendar;

/**
 * 韩永发
 *
 * @Date 16:39 2021/4/7
 */
public class demo1 {
  public static void main(String[] args) {
    //获取系统类加载器
    ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
    System.out.println(systemClassLoader);

    //获取其上层，扩展类加载器
    ClassLoader extCLassLoader = systemClassLoader.getParent();
    System.out.println(extCLassLoader);

    //获取其上层，试图获取bootstrap类加载器，引导类加载器
    ClassLoader bootStrapClassLoader = extCLassLoader.getParent();
    System.out.println(bootStrapClassLoader);//null

    //获取当前类的，ClassLoader,默认使用系统类加载器加载
    ClassLoader classLoader = demo1.class.getClassLoader();
    System.out.println(classLoader);

    //获取String的类加载器，string是用引导类加载器加载
    ClassLoader classLoader1 = String.class.getClassLoader();
    System.out.println(classLoader1);
    //java的核心都是由引导类加载器加载的


  }
}

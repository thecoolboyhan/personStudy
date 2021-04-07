package com.JVM.获取各种类加载器;

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

    //获取其上层，试图获取bootstrap类加载器
    ClassLoader parent = extCLassLoader.getParent();
    System.out.println(parent);//null
  }
}

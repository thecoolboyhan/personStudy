package com.JVM.获取各种类加载器;

import sun.misc.Launcher;

import java.net.URL;

/**
 * 韩永发
 *
 * @Date 19:30 2021/4/7
 */
public class BootStrapClassLoaderLei {
  public static void main(String[] args) {
    //获取bootStrap可以加载的所有包
    URL[] urLs = Launcher.getBootstrapClassPath().getURLs();
    for (URL urL : urLs) {
      System.out.println(urL.toExternalForm());
    }
  }
}

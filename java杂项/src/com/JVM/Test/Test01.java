package com.JVM.Test;

/**
 * 韩永发
 *
 * @Date 16:16 2021/4/2
 */
public class Test01 {
  public static int mun =1;
  static {
    mun=2;
    abb=1;
  }
  public static int abb=3;
  public static void main(String[] args) {
    System.out.printf("%d\n%d",Test01.mun,Test01.abb);//mun=2,abb=3
  }
}

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
//    System.out.printf("%d\n%d",Test01.mun,Test01.abb);//mun=2,abb=3S
    String s1="a";
    String s2="b";
    String s3="a"+"b";
    String s4=s1+s2;
    String s5="ab";
    String s6=s4.intern();

    System.out.println(s3 == s4);
    System.out.println(s3 == s5);
    System.out.println(s3 == s6);

    String x2=new String("c")+new String("d");
    String x1="cd";
    x2.intern();

    System.out.println(x1 == x2);

  }
}

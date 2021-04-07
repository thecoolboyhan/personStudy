package 线程的三大特性.可见性;

import jdk.nashorn.internal.ir.Flags;

import java.io.IOException;

/**
 * 韩永发
 *
 * @Date 15:05 2021/4/1
 */
public class Test02 {
  private static /*volatile*/ boolean running=true;
  private static void m(){
    System.out.println("m start");
    while (running){
//      System.out.println("hello");
    }
    System.out.println("m end");
  }

  public static void main(String[] args) throws IOException {
    new Thread(Test02::m,"ti").start();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    running=false;
    System.in.read();
  }
}

package 线程的三大特性.可见性;

import jdk.nashorn.internal.ir.Flags;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 韩永发
 *
 * @Date 15:05 2021/4/1
 */
public class Test02 {
  /*volatile*/ boolean running=true;
  void m(){
    System.out.println("m start");
    while (running){
//      System.out.println("hello");
    }
    System.out.println("m end");
  }

  public static void main(String[] args) throws IOException {
    Test02 test02 = new Test02();
    new Thread(test02::m,"ti").start();
    try {
      TimeUnit.SECONDS.sleep(1);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    test02.running=false;
//    System.in.read();
  }
}

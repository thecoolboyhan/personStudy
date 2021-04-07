package 线程的三大特性;

import java.util.concurrent.CountDownLatch;

/**
 * 韩永发
 *
 * @Date 14:01 2021/3/23
 */
public class kejianxing {

  public static long COUNT = 10_0000_0000L;

  //    @Contended
  private static class T{
    //        private long p1,p2,p3,p4,p5,p6,p7;
    public long x=0L;
//        private long p11,p12,p13,p14,p15,p16,p17;
  }
  public static T[] arr=new T[2];

  static {
    arr[0]= new T();
    arr[1]= new T();
  }

  public static void main(String[] args) throws InterruptedException {
    CountDownLatch countDownLatch = new CountDownLatch(2);
    Thread thread = new Thread(() -> {
      for (long i = 0; i < COUNT; i++) {
        arr[0].x = i;
      }
      countDownLatch.countDown();
    });

    Thread thread2 = new Thread(() -> {
      for (long i = 0; i < COUNT; i++) {
        arr[1].x = i;
      }
      countDownLatch.countDown();
    });

    final long l = System.nanoTime();
    thread.start();
    thread2.start();
    countDownLatch.await();
    System.out.println((System.nanoTime()-l)/100_0000);
  }
}

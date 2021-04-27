package 锁;

/**
 * 韩永发
 *
 * @Date 10:30 2021/4/16
 */
public class Test01 {
  public static void main(String[] args) {
    Test01 test01 = new Test01();
//    Test01 test02 = new Test01();
    new Thread(new Runnable() {
      @Override
      public void run() {
        test01.doLongTimeTask();
      }
    }).start();
    new Thread(new Runnable() {
      @Override
      public void run() {
        test01.doLongTimeTask();
      }
    }).start();
  }

  public void doLongTimeTask() {
    System.out.println(Thread.currentThread().getName() + "task begin");
    try {
      Thread.sleep(3000);
      synchronized (this) {
        System.out.println(Thread.currentThread().getName() + "开始同步");
        for (int i = 0; i < 100; i++) {
          System.out.println(Thread.currentThread().getName() + "-->" + i);
        }
      }
      System.out.println(Thread.currentThread().getName() + "task end!");
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}

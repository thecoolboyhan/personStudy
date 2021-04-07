package 线程的三大特性.原子性;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 韩永发
 *
 * @Date 9:30 2021/4/1
 */
public class Test01 {
  public static void main(String[] args) {

    MyInto myInt = new MyInto();
    for (int i = 0; i < 2; i++) {
      new Thread(new Runnable() {
        @Override
        public void run() {
          //两个线程操作是同一个对象中的同一个数据
          int i = 0;
          while (i<10){
            //
            System.out.println(Thread.currentThread().getName()+"->"+myInt.getNum());
            try {
              //读取其实是一个慢的过程，慢过数据处理的速度，所以休眠一下
              Thread.sleep(100);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
            i++;
          }
        }
      }).start();
    }

  }

  static class MyInt{
    int num;
    public int getNum(){
      return num++;
    }
  }

  //利用保证原子性的Atomic来实现原子性
  static class MyInto{
    AtomicInteger num=new AtomicInteger();
    public int getNum(){
      return num.incrementAndGet();//+1然后再返回
    }
  }
}

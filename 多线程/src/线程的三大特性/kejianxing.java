package 线程的三大特性;

/**
 * 韩永发
 *
 * @Date 14:01 2021/3/23
 */
public class kejianxing {

  public static void main(String[] args) {
	 Thread thread = new Thread();
	 thread.getState();
	 thread.yield();
  }
}

class abb extends Thread{

  @Override
  public void run() {
    String name = Thread.currentThread().getName();
    System.out.println("当前线程的名字："+name);
  }

}

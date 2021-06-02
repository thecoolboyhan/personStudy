package 锁;

import java.util.concurrent.Semaphore;

public class TestSemaphore {
    public static void main(String[] args) {
        //设定同时可以多少个线程来获得此锁,fair :设置是否为公平锁
        Semaphore semaphore = new Semaphore(2,true);
        new Thread(()->{
            try {
                //获得锁
                semaphore.acquire();
                System.out.println("t1 running");
                Thread.sleep(100);
                System.out.println("t1 running");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                //释放锁
                semaphore.release();
            }
        }).start();

        new Thread(()->{
            try {
                //获得锁
                semaphore.acquire();
                System.out.println("t2 running");
                Thread.sleep(100);
                System.out.println("t2 running");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                //释放锁
                semaphore.release();
            }
        }).start();

    }
}

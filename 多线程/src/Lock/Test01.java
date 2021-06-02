package Lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * lock的基本使用
 */
public class Test01 {
    //定义一个显示锁
    static Lock lock=new ReentrantLock();
    public static void out(){
        int x=1;
        lock.lock();
        for (int i = 0; i < 100; i++) {
            System.out.println(Thread.currentThread().getName()+"--"+i);
            if (i>50&&x==1){
                x=2;
                lock.unlock();
            }
        }
    }
    public static void main(String[] args) {
        Runnable runnable = () -> out();
        new Thread(runnable).start();
        new Thread(runnable).start();
        new Thread(runnable).start();
    }
}

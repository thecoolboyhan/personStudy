package 锁;

import javax.lang.model.element.VariableElement;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Test02 {
    static class SubThread extends Thread{
        private static Lock lock=new ReentrantLock();
        private static int mun =0;

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                mun++;
            }
        }
    }
    public static void main(String[] args) throws InterruptedException {
        SubThread subThread = new SubThread();
        SubThread subThread2 = new SubThread();
        subThread.start();
        subThread2.start();
        subThread.join();
        subThread2.join();
        System.out.println(SubThread.mun);
    }
}

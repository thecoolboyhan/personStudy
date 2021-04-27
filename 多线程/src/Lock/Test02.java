package Lock;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Test02 {
    static class IntLock implements Runnable{
        public static ReentrantLock lock1=new ReentrantLock();
        public static ReentrantLock lock2=new ReentrantLock();
        int num;
        public IntLock(int num){
            this.num=num;
        }
        @Override
        public void run() {
            try {
                if (num%2==1){
                    lock1.lockInterruptibly();
                    System.out.println(Thread.currentThread().getName()+"获得了锁一，还要锁2");
                    Thread.sleep(new Random().nextInt(500));
                    lock2.lockInterruptibly();
                    System.out.println(Thread.currentThread().getName()+"同时都获得");
                }else {
                    lock2.lockInterruptibly();
                    System.out.println(Thread.currentThread().getName()+"获得了锁2，还要锁1");
                    Thread.sleep(new Random().nextInt(500));
                    lock1.lockInterruptibly();
                    System.out.println(Thread.currentThread().getName()+"同时都获得");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (lock1.isHeldByCurrentThread())//判断当前
                lock1.unlock();
                lock2.unlock();
                System.out.println(Thread.currentThread().getName()+"两个线程结束了");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        IntLock intLock1 = new IntLock(11);
        IntLock intLock2 = new IntLock(22);
        Thread t1 = new Thread(intLock1);
        Thread t2 = new Thread(intLock2);
        t1.start();
        t2.start();
        Thread.sleep(3000);
        //如果t1没结束
        if (t1.isAlive()){
            t1.interrupt();
        }
        if (t2.isAlive()) {
            t2.interrupt();
        }
    }
}

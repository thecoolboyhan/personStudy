package com.java8特性.Lambda;



import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class AAC {

    private static final long count=100001;
    public static void main(String[] args) throws InterruptedException {
        Runner one = new Runner();
        Thread thread = new Thread(one, "CountThread");
        thread.start();
        TimeUnit.SECONDS.sleep(1);
        thread.interrupt();
        Runner two = new Runner();
        thread = new Thread(two, "CountThread");
        thread.start();
        TimeUnit.SECONDS.sleep(1);
        two.cancel();

    }

}

class Runner implements Runnable{

    private long i;

    private volatile boolean on =true;

    @Override
    public void run() {
        while (on && !Thread.currentThread().isInterrupted()){
            i++;
        }
        System.out.println("Count i="+i);
    }

    public void cancel(){
        on = false;
    }
}
package com.java8特性.Lambda;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import static java.util.concurrent.Executors.*;

/**
 * 使用semaphore来实现的限流
 */
public class AAD {
    private static final int THREAD_COUNT= 30;
    private static ExecutorService threadExecutor = Executors.newFixedThreadPool(10);
    private static Semaphore s= new Semaphore(5);

    public static void main(String[] args) {
        for (int i = 0; i < THREAD_COUNT; i++) {
            threadExecutor.execute(() -> {
                try {
                    //获得锁，型号量减1
                    s.acquire();
                    System.out.println(Thread.currentThread().getName() + "正在工作..");
                    Thread.sleep(10000);
                    System.out.println(Thread.currentThread().getName() + "工作结束..");
                    //释放锁信号量加1
                    s.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        threadExecutor.shutdown();
    }
}

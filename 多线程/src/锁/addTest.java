package 锁;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.ReentrantLock;

public class addTest {
    static long count2=0L;
    static AtomicLong count1=new AtomicLong(0L);
    static LongAdder count3=new LongAdder();

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[1000];

        for (int i = 0; i < threads.length; i++) {
                threads[i]=new Thread(()->{
                    for (int i1 = 0; i1 < 100000; i1++) {
                        count1.incrementAndGet();
                    }
                });
        }

        long l = System.currentTimeMillis();
        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        long l1 = System.currentTimeMillis();

        System.out.println("Atomic:"+count1.get()+"time"+(l1-l));

        Object lock = new Object();

        for (int i = 0; i < threads.length; i++) {
            threads[i]=new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i1 = 0; i1 < 100000; i1++) {
                        synchronized (lock){
                            count2++;
                        }
                    }
                }
            });
        }

        long start1 = System.currentTimeMillis();

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
        long end1 = System.currentTimeMillis();

        System.out.println("synchronized:"+count2+"time:"+(end1-start1));

        for (int i = 0; i < threads.length; i++) {
            threads[i]=new Thread(()->{
                for (int i1 = 0; i1 < 100000; i1++) {
                    count3.increment();
                }
            });
        }

        long start2 = System.currentTimeMillis();

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
        long end2 = System.currentTimeMillis();

        System.out.println("adder:"+count3.longValue()+"time:"+(end2-start2));

    }
}

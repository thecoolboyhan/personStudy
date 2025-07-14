package Concurrency.ForkJoinPool;

import java.util.concurrent.*;

public class ExecutorTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        用线程池计算从1到10000的累加
        ThreadPoolExecutor threadPoolExecutor= new ThreadPoolExecutor(10,100,1000, TimeUnit.MILLISECONDS,new LinkedBlockingDeque<>());
//        分两个线程计算，计算速度应对是单线程的两倍
        Future<Integer> future1 = threadPoolExecutor.submit(() -> {
            int sum = 0;
            for (int i = 0; i < 5000; i++) {
                sum += i;
            }
            return sum;
        });
        Future<Integer> future2 = threadPoolExecutor.submit(() -> {
            int sum = 0;
            for (int i = 5000; i <= 10000; i++) {
                sum += i;
            }
            return sum;
        });
        Integer s1 = future1.get();
        Integer s2 = future2.get();
        threadPoolExecutor.shutdown();
        System.out.println(s1+s2);
    }
}

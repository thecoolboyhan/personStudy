package Concurrency.ForkJoinPool;

import java.util.concurrent.*;

public class ForkJoinExecutorTest1 {
    public static void main(String[] args) {

//        拆封核心线程来运行任务，共拆封10个
        try(ForkJoinPool forkJoinPool = new ForkJoinPool(10)){
            SumTask sumTask = new SumTask(1, 10000);
            ForkJoinTask<Long> future = forkJoinPool.submit(sumTask);
//            如果检测到错误，输出错误信息
            if (future.isCompletedAbnormally()) System.out.println(future.getException());
//            Thread.sleep(5000);
            System.out.println(future.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}

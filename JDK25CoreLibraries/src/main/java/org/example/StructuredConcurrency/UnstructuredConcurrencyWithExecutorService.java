package org.example.StructuredConcurrency;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UnstructuredConcurrencyWithExecutorService {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            Future<String> user = executor.submit(UnstructuredConcurrencyWithExecutorService::findUser);
            Future<Integer> order = executor.submit(UnstructuredConcurrencyWithExecutorService::fetchOrder);
//            执行get方法时，主线程会阻塞，等待子任务都执行结束后，才会继续运行
            String theUser = user.get();
            Integer theOrder = order.get();
            System.out.println("user:"+theUser+",order:"+theOrder);
        }
    }
    private static int fetchOrder(){
        return 1;
    }
    private static String findUser(){
        return "rose";
    }
    
}

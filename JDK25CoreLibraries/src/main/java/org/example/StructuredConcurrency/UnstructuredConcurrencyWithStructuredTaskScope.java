package org.example.StructuredConcurrency;

import java.util.concurrent.StructuredTaskScope;

public class UnstructuredConcurrencyWithStructuredTaskScope {
    public static void main(String[] args) {
        long l = System.currentTimeMillis();
        try(var scope=StructuredTaskScope.open()){
            StructuredTaskScope.Subtask<Integer> user= scope.fork(()->fetchOrder());
            StructuredTaskScope.Subtask<String> order= scope.fork(()->findUser());
            scope.join();
            System.out.println("user:"+user.get()+",order:"+order.get());
        }catch (Exception e){
//            捕获并不处理异常，来统计父任务运行时间
        }
//        运行耗时
        System.out.println("父任务聚合结果耗时："+(System.currentTimeMillis()-l));
    }
    private static int fetchOrder() {
        long l = System.currentTimeMillis();
        try {
            Thread.sleep(10000l);
        } catch (InterruptedException e) {
//            捕获异常统计子任务运行时间
            System.out.println("其他子任务出现异常，打断当前任务运行:"+(System.currentTimeMillis()-l));
            return 1;
        }
        System.out.println("运行结束，正常返回");
        return 1;
    }
    private static String findUser() throws InterruptedException {
//        模拟抛异常，看看是否会打断另一个线程
        Thread.sleep(2000l);
//        两秒后抛出异常，打断另一个线程
        throw new RuntimeException("123");
//        return "rose";
    }
}

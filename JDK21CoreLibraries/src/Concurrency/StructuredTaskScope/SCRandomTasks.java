package Concurrency.StructuredTaskScope;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.stream.IntStream;

public class SCRandomTasks {

    class TooSlowException extends Exception {
        TooSlowException(String s){
            super(s);
        }
    }

    /**
     分别启动5个任务，调用成功关闭和失败关闭。
     */
    public static void main(String[] args) {
        var myApp = new SCRandomTasks();
        try{
            System.out.println("Running handleShutdownOnFailure...");
            myApp.handleShutdownOnFailure();
        } catch (ExecutionException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        try{
            System.out.println("Running handleShutdownOnSuccess...");
            myApp.handleShutdownOnSuccess();
        } catch (ExecutionException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public Integer randomTask(int max,int threshold) throws TooSlowException, InterruptedException {
        int t = new Random().nextInt(max);
        System.out.println("Duration:"+t);
        if(t>threshold) throw new TooSlowException(STR."Duration \{t} greater than threshold \{threshold}");
        Thread.sleep(t);
        return t;
    }

    void handleShutdownOnSuccess() throws InterruptedException, ExecutionException {
        try(var scope=new StructuredTaskScope.ShutdownOnSuccess()){
            IntStream.range(0,5)
                    .mapToObj(i->scope.fork(()->randomTask(1000,850)))
                    .toList();
            scope.join();
//            捕获第一个完成的子任务，并返回其结果。
            System.out.println(STR."First task to finish: \{scope.result()}");
        }
    }

    void handleShutdownOnFailure() throws InterruptedException, ExecutionException {
        try(var scope=new StructuredTaskScope.ShutdownOnFailure()){
//            var t= new SCRandomTasks();
            var subtasks= IntStream.range(0,5)
                    .mapToObj(i->scope.fork(new Callable<Integer>() {
                        @Override
                        public Integer call() throws Exception {
                            return randomTask(1000,850);
                        }
                    }))
                    .toList();
//            捕获子任务抛出的第一个异常，然后调用该方法:中断所有新的子任务启动，中断所有正在运行的其他子任务线程，并让主程序继续执行。
            scope.join()
                    .throwIfFailed();
            var totalDuration=subtasks.stream()
                    .map(StructuredTaskScope.Subtask::get)
                    .reduce(0,Integer::sum);
            System.out.println(STR."Total Duration:\{totalDuration}");
        }

    }
}

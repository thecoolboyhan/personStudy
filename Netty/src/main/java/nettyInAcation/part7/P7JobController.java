package nettyInAcation.part7;

import io.netty.channel.Channel;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class P7JobController {
//    jdk是如何做任务调度的
    public void jdkC(){
//        创建一个任务线程池
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
//        每60秒执行一次
            executor.schedule(() -> System.out.println("60 seconds later"), 60, TimeUnit.SECONDS);

    }
//    利用EventLoop停止一个任务
    public void eventLoopC(Channel ch){
//        通过EventLoop创建一个任务调度，60s后开始，每60s执行一次
        ScheduledFuture<?> future = ch.eventLoop().scheduleAtFixedRate(() -> System.out.println("60s seconds later"), 60, 60, TimeUnit.SECONDS);
//        创建一个停止任务的表示
        boolean mayInterruptIfRunning=false;
//        取消任务
        future.cancel(mayInterruptIfRunning);
    }


    public static void main(String[] args) {
        char a='!';
        boolean b=a=='=';
    }
}

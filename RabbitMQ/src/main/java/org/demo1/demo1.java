package org.demo1;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class demo1 {
    public static void main(String[] args) {
        BlockingQueue<KouZhao> que = new ArrayBlockingQueue<>(20);

        //生产快，消费慢
        new Thread(new Producer(que)).start();

        new Thread(new Consumer(que)).start();


    }
}

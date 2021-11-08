package com.JVM.直接内存释放对象;

import java.util.ArrayList;

/**
 * 测试GC分析参数
 * -Xms20M -Xmx20M -Xmn10M -XX:+UseSerialGC -XX:+PrintGCDetails -verbose:gc
 * 初始堆20M 最大堆20M 新生代10M 使用单线程垃圾回收 打印GC详情
 * @author rose
 */
public class Demo {

    private static final int _512kb =512*1024;
    private static final int _1Mb =1024*1024;
    private static final int _6Mb =6*1024*1024;
    private static final int _7Mb = 7*1024*1024;
    private static final int _8Mb = 8*1024*1024;

    public static void main(String[] args) throws InterruptedException {
        new Thread(()->{
            ArrayList<byte[]> list = new ArrayList<>();
            list.add(new byte[_8Mb]);
            list.add(new byte[_8Mb]);
        }).start();
        System.out.println("sleep...");
        Thread.sleep(10000);

    }
}

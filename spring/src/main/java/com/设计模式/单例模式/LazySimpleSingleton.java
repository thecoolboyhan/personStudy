package com.设计模式.单例模式;

/**
 * @author rose
 */
public class LazySimpleSingleton {

    private LazySimpleSingleton(){}

    private volatile static LazySimpleSingleton lazySimpleSingleton=null;

    public static LazySimpleSingleton getInstance(){
        if (lazySimpleSingleton==null) {
            synchronized (LazySimpleSingleton.class){
                if (lazySimpleSingleton == null) {
                    lazySimpleSingleton = new LazySimpleSingleton();
                }
            }
        }
        return lazySimpleSingleton;
    }
}

//线程类
class ExectorThread implements Runnable{
    public void run() {
        LazySimpleSingleton instance = LazySimpleSingleton.getInstance();
        System.out.println(Thread.currentThread().getName()+":"+instance);
    }
}

class LazySimpleSingletonTest{
    public static void main(String[] args) {
        Thread thread1 = new Thread(new ExectorThread());
        Thread thread2 = new Thread(new ExectorThread());
        thread1.start();
        thread2.start();
        System.out.println("end");
    }
}
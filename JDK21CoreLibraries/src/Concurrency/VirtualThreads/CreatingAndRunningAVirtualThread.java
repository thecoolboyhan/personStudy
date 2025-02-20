package Concurrency.VirtualThreads;

import org.w3c.dom.ls.LSOutput;

public class CreatingAndRunningAVirtualThread {

    public static void main(String[] args) throws InterruptedException {
        createByThreadBuilder2();
    }

    private static void createByThreadClass() throws InterruptedException {
        Thread thread = Thread.ofVirtual().start(() -> System.out.println("Hello"));
//        join是为了让虚拟线程插队到主线程之前，保证在主线程结束之前可以看到虚拟线程的打印
        thread.join();
    }

    /**
     * Thread.Builder builder = Thread.ofVirtual().name("MyThread");
     * Runnable task = () -> {
     *     System.out.println("Running thread");
     * };
     * Thread t = builder.start(task);
     * System.out.println("Thread t name: " + t.getName());
     * t.join();
     */

    private static void createByThreadBuilder1() throws InterruptedException {
        Thread.Builder builder = Thread.ofVirtual().name("virtualThread");
//        同样可以用来创建平台线程,所有其他API都兼容
//        Thread.Builder builder = Thread.ofPlatform().name("PlatformThread");
        Runnable task= () -> {
            System.out.println("Running thread");
        };
        Thread t = builder.start(task);
        System.out.println("Thread t name"+t.getName());
        t.join();
    }

    /**
     * Thread.Builder builder = Thread.ofVirtual().name("worker-", 0);
     * Runnable task = () -> {
     *     System.out.println("Thread ID: " + Thread.currentThread().threadId());
     * };
     *
     * // name "worker-0"
     * Thread t1 = builder.start(task);
     * t1.join();
     * System.out.println(t1.getName() + " terminated");
     *
     * // name "worker-1"
     * Thread t2 = builder.start(task);
     * t2.join();
     * System.out.println(t2.getName() + " terminated");
     */
    private static void createByThreadBuilder2() throws InterruptedException {
        Thread.Builder builder=Thread.ofVirtual().name("worker-",0);
        Runnable task=()->{
            System.out.println("Thread ID:"+Thread.currentThread().threadId());
        };
//        启动后会总动给start+1.
        Thread t1 = builder.start(task);
        t1.join();
        System.out.println(t1.getName()+" terminated");
        Thread t2 = builder.start(task);
        t2.join();
        System.out.println(t2.getName()+" terminated");
    }
}

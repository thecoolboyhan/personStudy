package 管道通信;

import javax.xml.crypto.Data;
import java.util.Date;

public class TestThreadLocal {
    public static void main(String[] args) {
//        SubThread subThread = new SubThread();
//        SubThread subThread1 = new SubThread();
//        subThread.start();
//        subThread1.start();
        ThreadlocalE threadlocalE=new ThreadlocalE();
        System.out.println(threadlocalE.get());

    }
    static ThreadLocal threadLocal=new ThreadLocal();
    static class SubThread extends Thread{
        @Override
        public void run() {
            for (int i = 0; i < 20; i++) {
                threadLocal.set(Thread.currentThread().getName()+"--"+i);
                System.out.println(Thread.currentThread().getName()+"value:"+threadLocal.get());
            }
        }
    }

    /**
     * 通过重写initialValue方法来给ThreadLocal赋初始值
     */
    static class ThreadlocalE extends ThreadLocal<Date>{
        @Override
        protected Date initialValue() {
            return new Date(System.currentTimeMillis()-1000*60*20);
        }
    }
}

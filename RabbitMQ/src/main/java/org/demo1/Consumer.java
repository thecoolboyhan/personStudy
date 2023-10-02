package org.demo1;

import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {
    private BlockingQueue<KouZhao> queue;
    public Consumer(BlockingQueue<KouZhao> queue){
        this.queue=queue;
    }

    @Override
    public void run() {

        while (true){
            try {
                    Thread.sleep(2000);
                    System.out.println("正在准备买口罩。。。。");
                    KouZhao kouZhao = queue.take();
                    System.out.println("买到了第"+kouZhao.getId()+"个"+kouZhao.getType());
            } catch (InterruptedException e) {
                    throw new RuntimeException(e);
            }
        }
    }
}

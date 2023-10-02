package org.demo1;

import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {

    private BlockingQueue<KouZhao> queue;

    public Producer(BlockingQueue<KouZhao> queue){
        this.queue=queue;
    }

    private Integer index=0;
    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(1000);
                if(queue.remainingCapacity()<=0){
                    System.out.println("口罩已经堆积如山了，先不生产");
                    continue;
                }
                KouZhao kouZhao=new KouZhao();
                kouZhao.setType("N95");
                kouZhao.setId(index++);
                System.out.println("正在生产第"+index+"个口罩");
                queue.put(kouZhao);
                System.out.println("目前还有的口罩数量"+queue.size());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }
}

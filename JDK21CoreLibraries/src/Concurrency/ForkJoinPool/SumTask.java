package Concurrency.ForkJoinPool;

import java.util.concurrent.RecursiveTask;

public class SumTask extends RecursiveTask<Long> {

    private final int l;
    private final int r;

    public SumTask(int l, int r) {
        this.l = l;
        this.r = r;
    }
    @Override
    protected Long compute() {
        long res=0;
//        判断任务是否拆封
        if(r-l<100){
            for (int i = l; i <=r; i++) {
                res+=i;
            }
            return res;
        }
//        需要被拆封的情况：经典二分查找
        int mid=(l+r)/2;
        SumTask leftTask=new SumTask(l,mid);
        SumTask rightTask=new SumTask(mid+1,r);
        leftTask.fork();
        rightTask.fork();
        long left=leftTask.join();
        long right=rightTask.join();
        return left+right;
    }
}

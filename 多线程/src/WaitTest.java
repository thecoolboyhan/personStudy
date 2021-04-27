import java.util.ArrayList;
import java.util.List;

public class WaitTest {
    public static void main(String[] args) {

        ThreadAdd threadAdd = new ThreadAdd();
        ThreadSub threadSub = new ThreadSub();
        threadSub.setName("thread11");
        threadSub.start();
        threadAdd.start();

    }
    static List list= new ArrayList();

    public static void subtract(){
        synchronized (list){
            if (list.isEmpty()){
                try {
                    System.out.println(Thread.currentThread().getName()+"begin wait ....");
                    list.wait();
                    System.out.println(Thread.currentThread().getName()+"end wait.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Object remove = list.remove(0);
            System.out.println(Thread.currentThread().getName()+"从集合中取出了"+remove+"集合里还有数量"+list.size());

        }
    }
    //向集合里添加数据
    public static void add(){
        synchronized (list){
           list.add("data");
           list.notifyAll();
        }
    }

    static class ThreadAdd extends Thread{
        @Override
        public void run() {
            add();
        }
    }
    static class ThreadSub extends Thread{
        @Override
        public void run() {
            subtract();
        }
    }

}

package 管道通信;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ThreadLocalTest2 {
    private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
    static ThreadLocal<SimpleDateFormat> threadLocal=new ThreadLocal<>();

    static class parseDate implements Runnable{
        private int i=0;
        public parseDate(int i){
            this.i=i;
        }
        @Override
        public void run() {
            try {
                String text="2068年11月22日 08:28:"+i%60;
//                Date parse = sdf.parse(text);
                if (threadLocal.get()==null){
                    threadLocal.set(new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss"));
                }
                Date parse = threadLocal.get().parse(text);
                System.out.println(i+"--"+parse);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(new parseDate(i)).start();
        }
    }
}

package 管道通信;

import javax.lang.model.element.VariableElement;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * 用管道实现两个线程之间的通信
 */
public class Test01 {
    public static void main(String[] args) throws IOException {

        //创建管道
        PipedInputStream pipedInputStream = new PipedInputStream();
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        //给两个管道建立连接
        pipedInputStream.connect(pipedOutputStream);
        //新建两个线程让他们通信
        new Thread(new Runnable() {
            @Override
            public void run() {
                writeData(pipedOutputStream);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                readData(pipedInputStream);
            }
        }).start();
    }

    //向管道流中写入数据
    public static void writeData(PipedOutputStream out) {
        try {
            for (int i = 0; i < 100; i++) {
                String data = i+",";
                out.write(data.getBytes());
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //从管道流中读取数据
    public static void readData(PipedInputStream in){
        byte[] bytes = new byte[1024];
        try {
            int read = in.read(bytes);
            while (read!=-1){
                System.out.println(new String(bytes,0,read));
                read = in.read(bytes);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

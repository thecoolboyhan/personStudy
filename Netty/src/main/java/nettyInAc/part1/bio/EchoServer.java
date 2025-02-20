package nettyInAc.part1.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * 基于bio的实现
 */
public class EchoServer {

    public void serve(int port) throws IOException{
        //利用socket创建一个ServerSocket
        final ServerSocket socket = new ServerSocket(port);
        try {
            while (true){
                //当ServerSocket接收到连接
                final Socket clientSocket = socket.accept();
                System.out.println("Accepted connection from" + clientSocket);
                //新建一个线程来处理连接请求
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //利用输入输出流来处理数据的数据
                            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                            writer.println(reader.readLine());
                            writer.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                            try {
                                clientSocket.close();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }

                    }
                }).start();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

class Solution {
    public int[] countMentions(int numberOfUsers, List<List<String>> events) {
        int[][] tt=new int[numberOfUsers][2];
        //上线
        PriorityQueue<int[]> pq1=new PriorityQueue<>((a,b)->a[0]-b[0]);
        //通知
        PriorityQueue<int[]> pq2=new PriorityQueue<>((a,b)->a[0]-b[0]);
        for (List<String> list : events) {
            int time=Integer.parseInt(list.get(1));
            if (list.get(0).equals("OFFLINE")){
                pq1.offer(new int[]{time,Integer.valueOf(list.get(2))});
                continue;
            }
            if(list.get(2).equals("ALL")){
                for (int[] t1 : tt) {
                    t1[0]++;
                }
                continue;
            }
            if(list.get(2).equals("HERE")){
                for (int i = 0; i < tt.length; i++) {
                    pq2.offer(new int[]{time,i});
                }
                continue;
            }
            String[] ss = list.get(2).split(" ");
            for (String s : ss) {
                pq2.offer(new int[]{time,Integer.valueOf(s)});
            }
        }
        while(!pq2.isEmpty()){
            int[] t1=pq2.poll();
            int time =t1[0];
            while(!pq1.isEmpty()&&pq1.peek()[0]<=time){
                int[] nums=pq1.poll();
                tt[nums[1]][1]=nums[0]+60;
            }
            if(tt[t1[1]][1]<=time){
                tt[t1[1]][0]++;
            }
        }
        int[] res=new int[tt.length];
        for (int i = 0; i < res.length; i++) {
            res[i]=tt[i][0];
        }
        return res;
    }
}

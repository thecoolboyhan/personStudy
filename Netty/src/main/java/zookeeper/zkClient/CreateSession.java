package zookeeper.zkClient;

import org.I0Itec.zkclient.ZkClient;

public class CreateSession {
    //用ZKClient完成会话创建
    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient("192.168.1.65:2181");
        //将ZK异步创建会话的过程同步化
        System.out.println("会话被创建");

    }
}

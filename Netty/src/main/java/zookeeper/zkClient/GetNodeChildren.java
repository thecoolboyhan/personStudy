package zookeeper.zkClient;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;

public class GetNodeChildren {
    //用ZKClient完成会话创建
    public static void main(String[] args) throws InterruptedException {
        ZkClient zkClient = new ZkClient("192.168.1.65:2181");
        //将ZK异步创建会话的过程同步化
        System.out.println("会话被创建");
        //获取子节点列表
        List<String> children = zkClient.getChildren("/zkClient-a");
        System.out.println(children);

        //注册监听事件,如果没有这个节点也可以监听
        zkClient.subscribeChildChanges("/zkClient-a-get", new IZkChildListener() {
            /**
             *
             * @param s 父节点路径
             * @param list 变化后的节点列表
             * @throws Exception 异常
             */
            @Override
            public void handleChildChange(String s, List<String> list) throws Exception {
                System.out.println(s+"的子节点发生了改变，变化后的子节点列表为:"+list);
            }
        });
        //测试
        zkClient.createPersistent("/zkClient-a-get");
        Thread.sleep(1000);
        zkClient.createPersistent("/zkClient-a-get/c1");
        Thread.sleep(1000);
    }
}

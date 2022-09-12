package zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class CreateSession {
    //创建会话
    public static void main(String[] args) {
        //普通风格
        RetryPolicy exponentialBackoffRetry = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("192.168.1.65:2181", exponentialBackoffRetry);
        curatorFramework.start();
        System.out.println("会话被建立了");

        //使用风格
        CuratorFramework base = CuratorFrameworkFactory.builder()
                .connectString("192.168.1.65:2181")
                .sessionTimeoutMs(50000)
                .connectionTimeoutMs(30000)
                .retryPolicy(exponentialBackoffRetry)
                .namespace("base")
                .build();
        base.start();
        System.out.println("会话2被创建了");
    }
}

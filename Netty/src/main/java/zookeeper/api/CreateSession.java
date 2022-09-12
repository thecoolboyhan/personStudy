package zookeeper.api;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class CreateSession implements Watcher {

    private static final CountDownLatch countDownLatch=new CountDownLatch(1);
    private static ZooKeeper zooKeeper;

    /**
     * 建立会话
     *
     * @param args
     */
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {

        //创建一个Zk实例连接ZK服务器
        zooKeeper = new ZooKeeper("192.168.1.65:2181", 5000, new CreateSession());
        //ZK客户端会话创建是一个异步的过程，上面的构造方法在处理完客户端初始化工作后，会立刻返回
        //当会话真正创建完成后，ZK服务器会向客户端发送一个事件通知来告诉客户端端。
        System.out.println(zooKeeper.getState());

        //利用CountDownLatch让线程处于阻塞状态
//        countDownLatch.await();
        Thread.sleep(Integer.MAX_VALUE);
        System.out.println("客户端和服务器会话创建成功！");
        System.out.println(zooKeeper.getState());

        //创建节点
        createNodeSync();
    }

    /**
     * 获取节点的数据
     */
    private static void getNodeData() throws InterruptedException, KeeperException {
        //获取节点的内容
        byte[] data = zooKeeper.getData("/demo-aa", false, null);
        System.out.println(new String(data));
    }

    /**
     * 获取某个节点的字节点
     */
    private static void getChildren() throws InterruptedException, KeeperException {
        List<String> children = zooKeeper.getChildren("/demo-aa", true);
        System.out.println(children);
    }

    private static void createNodeSync() throws InterruptedException, KeeperException {
        //持久节点,完全开放权限
        String aa = zooKeeper.create("/demo-per", "持久节点内容".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        //临时节点
        String bb = zooKeeper.create("/demo-eph", "临时节点内容".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        //持久顺序节点
        String cc = zooKeeper.create("/demo-per_seq", "持久顺序节点内容".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
        System.out.println("aa");
        System.out.println("bb");
        System.out.println("cc");
    }

    /**
     * 回调方法：处理来自服务器的watcher通知
     */
    @Override
    public void process(WatchedEvent watchedEvent) {

        //当监听的子节点列表发生改变，服务器会发出事件通知
        //要重新获取节点列表，这个通知是一次性的，需要反复注册监听
        if(watchedEvent.getType()==Event.EventType.NodeChildrenChanged){
            List<String> children=null;
            try {
                children = zooKeeper.getChildren("/demo-aa", true);
            } catch (KeeperException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("节点列表发生了改变"+children);
        }

        //SyncConnected:会话创建成功事件
        if (watchedEvent.getState()==Event.KeeperState.SyncConnected){
            System.out.println("服务器会话创建成功");
            //解除CountDownLatch的阻塞状态
//            countDownLatch.countDown();
            //创建节点
            try {
                createNodeSync();
            } catch (InterruptedException | KeeperException e) {
                throw new RuntimeException(e);
            }

            try {
                //获取节点数据
                getNodeData();
                //获取某个节点的子节点
                getChildren();
            } catch (InterruptedException | KeeperException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //更新数据节点内容
    private static void updateNodeSync() throws InterruptedException, KeeperException {
        byte[] data1 = zooKeeper.getData("/demo-aa", false, null);
        System.out.println("修改前的值："+new String(data1));
        //修改节点的数据
        Stat stat = zooKeeper.setData("/demo-aa", "客户端修改数据".getBytes(), -1);
        byte[] data2 = zooKeeper.getData("/demo-aa", false, null);
        System.out.println("修改后的值："+new String(data2));
    }

    //删除节点
    private static void deleteNodeSync() throws InterruptedException, KeeperException {
        //判断节点是否被删除
        Stat exists = zooKeeper.exists("/demo-aa", false);
        System.out.println(exists==null?"该节点不存在" : "该节点存在");
        if (exists!=null){
            //删除节点
            zooKeeper.delete("/demo-aa",-1);
        }
        Stat exists2 = zooKeeper.exists("/demo-aa", false);
        System.out.println(exists2==null?"该节点不存在" : "该节点存在");

    }

}

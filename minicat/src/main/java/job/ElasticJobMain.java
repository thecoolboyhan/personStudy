package job;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;

public class ElasticJobMain {
    public static void main(String[] args) {
        //配置分布式协议服务（注册中心）Zookeeper
        ZookeeperConfiguration zookeeperConfiguration=new ZookeeperConfiguration("192.168.1.65:2181","archive-job");
        CoordinatorRegistryCenter coordinatorRegistryCenter=new ZookeeperRegistryCenter(zookeeperConfiguration);
        coordinatorRegistryCenter.init();

        //配置任务（时间事件，定时任务业务逻辑，调度器）
        JobCoreConfiguration job = JobCoreConfiguration.newBuilder("job", "*/2 * * * * ?", 1).build();
        SimpleJobConfiguration simpleJobConfiguration = new SimpleJobConfiguration(job,ArchiveJob.class.getName());
        LiteJobConfiguration liteJobConfiguration = LiteJobConfiguration.newBuilder(simpleJobConfiguration).overwrite(true).build();
        //调度器来执行任务
        JobScheduler jobScheduler = new JobScheduler(coordinatorRegistryCenter, liteJobConfiguration);
        jobScheduler.init();

    }
}

package job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

import java.util.*;

/**
 * 定时任务业务处理类
 */
public class ArchiveJob implements SimpleJob {

    /**
     * execute方法中写我们的业务处理逻辑
     * 每次定时任务执行，都会执行一次
     * @param shardingContext
     */
    @Override
    public void execute(ShardingContext shardingContext) {
        System.out.println(System.currentTimeMillis()+"我在执行job了");
    }

}

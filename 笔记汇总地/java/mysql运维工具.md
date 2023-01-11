# MySql运维工具

## Yearning

> 开源mysql sql审核平台，提供数据库字典查询，查询审计，sql审核等多个功能。





## canal

> 基于mysql数据库增量日志解析，提供增量数据订阅和消费。

最早用来同步两个数据的数据。

- 工作原理

mysql主从复制原理

1. mysql主库把变更日志写入binlog
2. 从库把主库的binlog拷到它的中继日志中（relay log）
3. 从库重放relay log中的事件，将数据变更成它自己的数据。

canal的工作原理：

1. canal模拟了mysql从库的协议，把自己伪装成mysql从库，向mysql主库发送dump协议。
2. mysql主库收到dump协议，开始向canal推送binlog
3. canal把binlog解析成流，可以再发送给如kafka，elsearch，hbase等



## DataX

> 阿里巴巴离线数据同步工具，canal是实时数据同步。



- dataX设计思想

> 把不同来源的数据定义成不同的reader和writer。

reader：数据采集模块，负责采集数据源的数据，把数据发送给Framwork

writer：数据写入模块，负责不断从framwork取数据，并将数据写入到目的端。

Framwork：连接reader和writer，作为两者传输的通道，并处理缓冲，流控，并发，数据转换等核心技术问题。



## Percona-toolkit

> 一组高级命令行工具的集合，可以查看当前服务的摘要信息，磁盘检测，分析慢查询日志，查找重复索引，实现表同步等等。



## Mysql MTOP（Lepus)

> 一个由python+php开发的开源mysql企业监控系统，该系统由python实现多进程数据采集和告警，PHP实现web展示和管理。

- 优点：

1. mysql服务器无需安装任何Agent，只需要在监控web页面配置数据库信息。
2. 启动监控进程后，可对上百台mysql数据库的状态，连接数，QTS，TPS，数据库流量，复制，性能慢查询等进行实时监控。
3. 可以在数据库偏离设定的阈值（如连接异常，复制异常，复制延迟）发送告警邮件
4. 可以对历史数据归档，通过图表展示数据库近期状态。


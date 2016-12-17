## 项目规划

#### monitor_agent
[以下简称ma]
是agent检测,定期实时数据post请求到monitor_center|按时间存储到cassandra,需要展现
#### monitor_center
[以下简称mc]
是一个django1.6搭建的服务端,收集agent的post请求到redis数据库(set 可以覆盖每个key:host的这一刻信息)
界面使用echarts 展现实时访问redis的数据以及cassandra的历史数据

#### ma依赖模块
psutil、cassandra

#### mc依赖模块
django、redis、cassandra
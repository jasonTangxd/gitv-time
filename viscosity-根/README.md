## Viscosity黏度相关
#### 1.线上MR黏度批处理
每天计算当前的前30天的总播放时长top100以及占比情况

（1）通过shell脚本控制产生今天前的30天的数据目录

（2）通过mr接参决定输入路径

（3）结果分为直播、点播|playlength、zb(pl/set(mac))

（4）结果reduce输出到hdfs的两个不同文件中，通过sqoop导入150的mysql

（5）mysql中通过天数切表，每天访问不同表展示

![](http://i1.piimg.com/567571/bed59a0646e0c6e3.jpg)

#### 2.PingBack_log To Cassandra (Storm)

收集来自active_user发进kafka的数据,处理后数据入casandra的结构

如下图大红色和蓝色为主键(红色分区键、蓝色排序键)、玫红色为count类型

重复字段累加click_num、playlength
添加时间戳字段，只细分到天，为后面的读取cassandra到hdfs作为一天一同步的过滤条件

【liv_viscosity_program】

![](http://i1.piimg.com/567571/580c19d893e7bef4.png)

【liv_viscosity_user】

![](http://i1.piimg.com/567571/94f58880ab77ee4d.png)

【vod_viscosity_user】

![](http://i1.piimg.com/567571/ec05001cbe5cf0a9.png)

【vod_viscosity_program】

![](http://i1.piimg.com/567571/34861cbcf4bf9c01.png)

结合上游数据量，本环节处理：

topo基本数据qps：

#### 3.Cassandra to HDFS
```
10.10.121.148:crontab -l
```
```
45 00 * * * sh /data/opt/script/viscosity/tohdfs.sh > /data/opt/script/viscosity/sh_tohdfs.log 2>&1 &
```
![](http://i1.piimg.com/567571/30a327e62119980e.png)

启动四个java Application执行，导入hdfs后：

![](http://i1.piimg.com/567571/1720aa9dcdba7576.png)

执行结果会录入zk（成功 or 失败）：

![](http://p1.bqimg.com/567571/5cd06add07b4e344.png)

![](http://p1.bqimg.com/567571/1435d42ce4e5ccb9.png)
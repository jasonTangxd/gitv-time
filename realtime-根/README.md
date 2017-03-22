## 项目简介
统计总数&各个合作伙伴下 ``各频道人数``

区分``点播`` ``直播``用户

**预览图:**

![](http://p1.bpimg.com/567571/34fed37ff355a503.jpg)

----------------------------------------------------

![](http://p1.bpimg.com/567571/4a6a15a56c8d76c5.jpg)
----------
## 命令记录

启动topo
```
/data/opt/soft/apache-storm-1.0.1/bin/storm jar rt-liv.jar cn.gitv.bi.rtliv.usercount.start.Start_up realtime_page_liv
/data/opt/soft/apache-storm-1.0.1/bin/storm jar rt-vod.jar cn.gitv.bi.rtvod.usercount.start.Start_up realtime_page_vod
```
启动40上的node.js界面
```
nohup node www.js > nohup.out 2>&1 &
```
清空linux内存缓存
```
sync; echo 3 > /proc/sys/vm/drop_caches
```
启动redis

``因为一台机的三个库db1、db2、db3可能串库，无法使用库起到数据隔离作用,使用三个conf配置文件，一台机器启动三个redis服务实例``
```
/data/opt/soft/redis-3.2.1/src/redis-server /data/opt/soft/redis-3.2.1/redis1.conf
```
```
/data/opt/soft/redis-3.2.1/src/redis-server /data/opt/soft/redis-3.2.1/redis2.conf
```
```
/data/opt/soft/redis-3.2.1/src/redis-server /data/opt/soft/redis-3.2.1/redis3.conf
```
##Detail

#### 1.项目整体架构图
（点播为例，直播类似）

![](http://p1.bpimg.com/567571/59429566b11381a8.png)

#### 2.Storm处理逻辑
* 数据来源
数据来自40的nginx服务器收集的日志，然后通过flumn收集到kafka中，该topo的数据源来自kafka
* 处理逻辑图

![](http://p1.bpimg.com/567571/92a267838981be70.png)
```
realtime_spout:获取kafka中的数据，供bolt使用
distinguish：区分点播直播数据，分发到不同流
vod_filter：过滤所需数据字段
liv_filter:同上
vod_mac_redis:通过pt的值对mac用户进行选择性操作入redis
liv_mac_redis：同上
vod_sumto:计数入redis，实时统计。通过map缓存，1s一刷
liv_sumto：同上
liv_an:记录partner|channel---->an关系
```
* 数据处理逻辑
(以点播为例)

![](http://p1.bpimg.com/567571/53a5f67829722af0.png)

* 其他
因为使用twemproxy代理切片后，redis的multi事务以及watcher不可使用，命令的顺序无法保证，故存在上游数据停止后出现负数，但可保证上游正常供数据时不会出现。【支持multi的Codis需要go以及外网环境，线下机器无外网】

* ttl绑定列表

VOD:

![](http://p1.bpimg.com/567571/a8264bb89c8e886f.png)

LIV:

![](http://p1.bpimg.com/567571/6259dee612e1d79e.png)

## 项目小结
* 使用了twemproxy作hash切片，多台redis实现(抛弃)
* 使用了redis自身的ttl策略触发，ps：抽样到期失效
* 改用客户端hash选择redis连接、liv和vod分离
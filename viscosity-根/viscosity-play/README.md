## 项目简介
用户播放黏度计算,通过storm实时收集上游的topo发送的完整播放记录,统计用户和节目的viscosity

## 命令备忘
Topo命令
```
/data/opt/soft/apache-storm-1.0.1/bin/storm jar pv.jar cn.gitv.bi.viscosity.tvplay.start.Start_up play_viscosity
```
消费Kafka的命令
```
kafka-console-consumer.sh --zookeeper slave7.gitv.rack2.bk:2281,slave8.gitv.rack2.bk:2281,slave9.gitv.rack2.bk:2281,slave10.gitv.rack2.bk:2281,slave11.gitv.rack2.bk:2281,master2.gitv.rack2.bk:2281,master1.gitv.rack1.bk:2281,slave1.gitv.rack1.bk:2281,slave2.gitv.rack1.bk:2281,slave3.gitv.rack1.bk:2281,slave4.gitv.rack1.bk:2281 --topic day-filter-record --from-beginning|more
```

## Zk记录信息
> **srcID => srcName:**
```
create /play_viscosity/srcid_meaning/1 首屏推荐
create /play_viscosity/srcid_meaning/2 频道列表
create /play_viscosity/srcid_meaning/3 专题列表
create /play_viscosity/srcid_meaning/4 搜索列表
create /play_viscosity/srcid_meaning/5 热门搜索
create /play_viscosity/srcid_meaning/6 标签筛选
create /play_viscosity/srcid_meaning/7 频道推荐
create /play_viscosity/srcid_meaning/8 播放记录
create /play_viscosity/srcid_meaning/9 主创相关
create /play_viscosity/srcid_meaning/10 收藏列表
create /play_viscosity/srcid_meaning/11 视频连播
create /play_viscosity/srcid_meaning/12 频道热播列表
create /play_viscosity/srcid_meaning/13 频道好评列表
create /play_viscosity/srcid_meaning/14 播放中猜你喜欢
create /play_viscosity/srcid_meaning/15 播放完猜你喜欢
create /play_viscosity/srcid_meaning/16 详情页猜你喜欢
create /play_viscosity/srcid_meaning/17 推荐专题
create /play_viscosity/srcid_meaning/18 预告片列表
create /play_viscosity/srcid_meaning/19 热映列表
create /play_viscosity/srcid_meaning/20 第三方应用
create /play_viscosity/srcid_meaning/21 外露标签列表
create /play_viscosity/srcid_meaning/22 有效订购记录
create /play_viscosity/srcid_meaning/23 失效订购记录
create /play_viscosity/srcid_meaning/24 详情页
```

## 相关建表语句
```
CREATE KEYSPACE viscositys WITH replication = {'class': 'NetworkTopologyStrategy', 'datacenter1': '3'}  AND durable_writes = true;
```
```
CREATE TABLE viscositys.viscosity_user_nosrcid_mean (
    srcid text PRIMARY KEY
);
```
```
CREATE TABLE viscositys.vod_viscosity_program (
    partner text,
    logdate timestamp,
    srcname text,
    chnname text,
    chnId text,
    albumname text,
    albumId text,
    playorder text,
    videoId text,
    province text,
    city text,
    playlength counter,
    timelength counter,
    click_num counter,
    PRIMARY KEY (partner, logdate, srcname, chnname,chnId,albumname,albumId, playorder,videoId, province, city)
);
```
```
CREATE TABLE viscositys.vod_viscosity_user (
    partner text,
    logdate timestamp,
    mac text,
    srcname text,
    chnname text,
    chnId text,
    albumname text,
    albumId text,
    playorder text,
    videoId text,
    playlength counter,
    timelength counter,
    click_num counter,
    PRIMARY KEY (partner, logdate, mac, srcname, chnname,chnId,albumname,albumId, playorder,videoId)
);
```
```
CREATE TABLE viscositys.liv_viscosity_program (
    partner text,
    logdate timestamp,
    srcname text,
    chncode text,
    albumname text,
    playorder text,
    province text,
    city text,
    playlength counter,
    timelength counter,
    click_num counter,
    PRIMARY KEY (partner, logdate, srcname, chncode,albumname, playorder, province, city)
);
```
```
CREATE TABLE viscositys.liv_viscosity_user (
    partner text,
    logdate timestamp,
    mac text,
    srcname text,
    chncode text,
    albumname text,
    playorder text,
    playlength counter,
    timelength counter,
    click_num counter,
    PRIMARY KEY (partner, logdate, mac, srcname, chncode,albumname, playorder)
);
```
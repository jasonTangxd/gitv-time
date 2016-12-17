## 项目简介
``将cassandra实时存储的viscosity数据定期按天导入到hdfs上``的一个java application
## 启动定时调度
在148这台机器上
```
20 00 * * * sh /data/opt/script/viscosity/tohdfs.sh > /data/opt/script/viscosity/sh_tohdfs.log 2>&1 &
```
---
## 四个类型的建表
>
* vod_viscosity_user
```
CREATE TABLE IF NOT EXISTS vod_viscosity_user
   (partner STRING,
   logdate STRING,
   mac STRING,
   srcname STRING,
   chnname STRING,
   chnId STRING,
   albumname STRING,
   albumId STRING,
   playorder STRING,
   videoId STRING,
   click_num STRING,
   playlength STRING,
   timelength STRING)
   PARTITIONED BY(yyyy string,mm String,dd String)
   ROW FORMAT DELIMITED
   FIELDS TERMINATED BY '|'
   STORED AS SEQUENCEFILE;
```
* vod_viscosity_program
```
   CREATE TABLE IF NOT EXISTS vod_viscosity_program
   (partner STRING,
   logdate STRING,
   srcname STRING,
   chnname STRING,
   chnId STRING,
   albumname STRING,
   albumId STRING,
   playorder STRING,
   videoId STRING,
   province STRING,
   city STRING,
   click_num STRING,
   playlength STRING,
   timelength STRING)
   PARTITIONED BY(yyyy string,mm String,dd String)
   ROW FORMAT DELIMITED
   FIELDS TERMINATED BY '|'
   STORED AS SEQUENCEFILE;
```
* liv_viscosity_user
```
   CREATE TABLE IF NOT EXISTS liv_viscosity_user
   (partner STRING,
   logdate STRING,
   mac STRING,
   srcname STRING,
   chncode STRING,
   albumname STRING,
   playorder STRING,
   click_num STRING,
   playlength STRING,
   timelength STRING)
   PARTITIONED BY(yyyy string,mm String,dd String)
   ROW FORMAT DELIMITED
   FIELDS TERMINATED BY '|'
   STORED AS SEQUENCEFILE;
```
* liv_viscosity_program
```
   CREATE TABLE IF NOT EXISTS liv_viscosity_program
   (partner STRING,
   logdate STRING,
   srcname STRING,
   chncode STRING,
   albumname STRING,
   playorder STRING,
   province STRING,
   city STRING,
   click_num STRING,
   playlength STRING,
   timelength STRING)
   PARTITIONED BY(yyyy string,mm String,dd String)
   ROW FORMAT DELIMITED
   FIELDS TERMINATED BY '|'
   STORED AS SEQUENCEFILE;
```
## 项目简介
当logclean把数据发送到kafka后，使用hdfs-bolt-plus依赖实现用storm：``from kafka to hdfs``

对官方的hdfs-bolt修改开发增加自用时间断流策略
文件按照``大小`` | ``write连接时间``进行切分


## 命令记录
```
/data/opt/soft/apache-storm-1.0.1/bin/storm jar ~/launcher-to-hdfs.jar cn.gitv.bi.launcher.tohdfs.start.Start_up launcher-to-hdfs
```

## hive建表供spark sql使用
```
CREATE TABLE IF NOT EXISTS launcher_100
   (Action STRING,P STRING,MC STRING,V STRING,UID STRING,STBID STRING,TS STRING,OS STRING,HM STRING,UG STRING,LIC STRING,ip STRING,timestamp STRING,record_day STRING)
   PARTITIONED BY(yyyy string,mm String,dd String)
   ROW FORMAT DELIMITED
   FIELDS TERMINATED BY '|'
   STORED AS SEQUENCEFILE;
```
```
   CREATE TABLE IF NOT EXISTS launcher_101
   (Action STRING,P STRING,MC STRING,V STRING,UID STRING,STBID STRING,TS STRING,OS STRING,HM STRING,OV STRING,UG STRING,ip STRING,timestamp STRING,record_day STRING)
   PARTITIONED BY(yyyy string,mm String,dd String)
   ROW FORMAT DELIMITED
   FIELDS TERMINATED BY '|'
   STORED AS SEQUENCEFILE;
```
```
   CREATE TABLE IF NOT EXISTS launcher_102
   (Action STRING,P STRING,MC STRING,V STRING,UID STRING,STBID STRING,TS STRING,OS STRING,HM STRING,
   initType STRING,initData STRING,albumId STRING,chnId STRING,cpId STRING,type STRING,cpContentId STRING,topicLayout STRING,topicPicBg STRING,chnName STRING,currTypingCode STRING,
   currTypingName STRING,typings STRING,num STRING,tvId STRING,tagId STRING,pageName STRING,screenInfo STRING,picUrl STRING,
   SID STRING,AR STRING,X STRING,Y STRING,TNUM STRING,UG STRING,CTP STRING,ip STRING,timestamp STRING,record_day STRING)
   PARTITIONED BY(yyyy string,mm String,dd String)
   ROW FORMAT DELIMITED
   FIELDS TERMINATED BY '|'
   STORED AS SEQUENCEFILE;
```
```
   CREATE TABLE IF NOT EXISTS launcher_103
   (Action STRING,P STRING,MC STRING,V STRING,UID STRING,STBID STRING,TS STRING,OS STRING,HM STRING,
   initType STRING,initData STRING,albumId STRING,chnId STRING,cpId STRING,type STRING,cpContentId STRING,topicLayout STRING,topicPicBg STRING,chnName STRING,currTypingCode STRING,
   currTypingName STRING,typings STRING,num STRING,tvId STRING,tagId STRING,pageName STRING,screenInfo STRING,picUrl STRING,
   TP STRING,RE STRING,UG STRING,CTP STRING,ip STRING,timestamp STRING,record_day STRING)
   PARTITIONED BY(yyyy string,mm String,dd String)
   ROW FORMAT DELIMITED
   FIELDS TERMINATED BY '|'
   STORED AS SEQUENCEFILE;
```
```
   CREATE TABLE IF NOT EXISTS launcher_104
   (Action STRING,P STRING,MC STRING,V STRING,UID STRING,STBID STRING,TS STRING,OS STRING,HM STRING,TNUM STRING,OTN STRING,UG STRING,ip STRING,timestamp STRING,record_day STRING)
   PARTITIONED BY(yyyy string,mm String,dd String)
   ROW FORMAT DELIMITED
   FIELDS TERMINATED BY '|'
   STORED AS SEQUENCEFILE;
```

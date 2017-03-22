## 项目简介
这是一个对launcher日志清洗后入kafka的项目,其中Action=100、101、102、103、105
包含urlEncode、Json的解析,数据来源：flume收集nginx的日志入kafka后的topic

## 命令记录
flume启动
```
flume-ng agent -c conf -f bi-launcher-kafka-likang.conf --n launcher  -Djava.library.path= org.apache.flume.node.Application
```
消费kafka中的topic
```
kafka-console-consumer.sh --zookeeper slave7.gitv.rack2.bk:2281,slave8.gitv.rack2.bk:2281,slave9.gitv.rack2.bk:2281,slave10.gitv.rack2.bk:2281,slave11.gitv.rack2.bk:2281,master2.gitv.rack2.bk:2281,master1.gitv.rack1.bk:2281,slave1.gitv.rack1.bk:2281,slave2.gitv.rack1.bk:2281,slave3.gitv.rack1.bk:2281,slave4.gitv.rack1.bk:2281 --topic bi-launcher-test --from-beginning
```
处理完入kafka的topic查看(A=100为例)
```
kafka-console-consumer.sh --zookeeper slave7.gitv.rack2.bk:2281,slave8.gitv.rack2.bk:2281,slave9.gitv.rack2.bk:2281,slave10.gitv.rack2.bk:2281,slave11.gitv.rack2.bk:2281,master2.gitv.rack2.bk:2281,master1.gitv.rack1.bk:2281,slave1.gitv.rack1.bk:2281,slave2.gitv.rack1.bk:2281,slave3.gitv.rack1.bk:2281,slave4.gitv.rack1.bk:2281 --topic launcher-log-100 --from-beginning
```
数据摘要
```
http://pb.bi.gitv.tv/launcher_pb/v?A=100&P=GITV&UI=fc52d4ba679049b1a33352ad6dd4e24b&MC=30:f3:1d:ac:93:1c&V=1.0.0&UID=zhangSan&stbid=10000000200&TS=20141230140600&rom=gitv1.0
````
启动topo
```
/data/opt/soft/apache-storm-1.0.1/bin/storm jar ~/launcher-log-clean.jar cn.gitv.bi.launcher.logclean.start.Start_up launcher-log-clean
```

## 备忘
开始准备将错误数据记录在cassandra，后来改为用ELK
```
CREATE KEYSPACE launcher WITH replication = {'class': 'NetworkTopologyStrategy', 'datacenter1': '3'}  AND durable_writes = true;
```
```
CREATE TABLE launcher.url_data_ecp (
    utl_data text,
    happen_time timestamp,
    what_exception text,
    PRIMARY KEY(utl_data,happen_time)
);
```
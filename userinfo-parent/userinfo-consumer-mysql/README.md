## 项目介绍
该项目用作RabbitMQ的消费者模型，从项目组获取用户的一些基本信息(pingback中没有的信息)
使用``时间``和``数量``两个维度去消费mq中的数据

## 程序启动条件
rabbitMQ启动
```
nohup /data/soft/rabbitmq_server-3.6.5/sbin/rabbitmq-server > /data/soft/rabbitmq_server-3.6.5/sbin/nohup.out 2>&1 &
```
确认在rabbitmq成功启动,且需要的queue is not null的情况下:
```
nohup java -jar uif_consumer.jar > nohup.out 2>&1 &
```

## 简单小结
**about log**
>comman-logging +log4j

>log4j.properties put in src/main/resources  By using interface of logging will use log4j

>现在用的比较多的是slf4j,也是一个接口，需要用到的是slf4j-log4j12实现桥接，具体实现使用自己想用的log4j等.

**pom**

>学会用pom的依赖树,可以去除一些框架的依赖,然后单独倒入共用的依赖

## 导出用户信息表
#### cassandra中导出csv
```
COPY userinfo.user_info (partner,mac_addr,account_time,activate_time,area_name,child_account_id,city_name,first_open,last_open,main_account_id,livod_app_version,vod_app_version,cnm_app_version,launcher_app_version,province,record_date,status,status_updatetime,livod_update_history,vod_update_history,cnm_update_history,launcher_update_history,user_id,user_type) TO 'export.csv';
```
>ps csv导入cassandra
```
COPY user_info.user_info (partner,mac_addr,account_time,activate_time,area_name,child_account_id,city_name,first_open,last_open,main_account_id,livod_app_version,vod_app_version,cnm_app_version,launcher_app_version,province,record_date,status,status_updatetime,livod_update_history,vod_update_history,cnm_update_history,launcher_update_history,user_id,user_type) FROM 'export.csv'
```

#### 测试建表
```
use test;
CREATE external TABLE UIF(partner string,mac_addr string,account_time string,activate_time string,area_name string,
child_account_id string,city_name string,first_open string,last_open string,main_account_id string,livod_app_version string,
vod_app_version string,cnm_app_version string,launcher_app_version string,province string,record_date string,status int,status_updatetime string,
livod_update_history string,vod_update_history string,cnm_update_history string,launcher_update_history string,user_id string,user_type int)
ROW FORMAT DELIMITED FIELDS TERMINATED BY ','
STORED AS TEXTFILE
LOCATION '/ys';
```
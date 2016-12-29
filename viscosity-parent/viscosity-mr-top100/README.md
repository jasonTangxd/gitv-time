## 项目简介
用来统计各合作伙伴的各个频道下的top100的``播放总时长``和``播放占比``

使用mr区分直播和点播统计,使用sqoop对结果数据直接导入150的mysql

>该project使用了两个先后顺序的mr job

## 原始数据查看
**vod**
```
hadoop fs -text /data/log_cleaned/vod_cleaned/2016/01/01/*|more
```
**liv**
```
hadoop fs -text /data/log_cleaned/livod_cleaned/2016/10/01/*|more
```
## mysql出现乱码:
```
alter database viscosity character set utf8;
set character_set_connection=utf8;
set character_set_connection=utf8;
set character_set_results=utf8;
set character_set_server=utf8;
```

**可以修改配置文件**
>
[root@Hadoop48 ~]# vi /etc/my.cnf
>
```
[mysql]
default-character-set=utf8
```
```
[client]
default-character-set=utf8
```
```
[mysqld]
default-character-set=utf8
character_set_server=utf8
init_connect='SET NAMES utf8
```

重启mysql，这样确保缺省编码是utf8



## Sqoop
开始想通过分区表的方式10、11月区分，但是后来发现sqoop的限制，直接对mr跑完的数据导入mysql，所以无需hive建表了
>``将hive的数据导入到mysql(mysql的表必须提前创建好)``
```
create table liv_pl_top100_10(partner varchar(256),chncode varchar(256),albumname varchar(256),pl int);
```
```
create table liv_zb_top100_10(partner varchar(256),chncode varchar(256),albumname varchar(256),zb double);
```
```
ALTER TABLE liv_pl_top100_10 CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
```
```
ALTER TABLE liv_zb_top100_10 CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
```
>sqoop导入myslq
```
sqoop export -connect "jdbc:mysql://10.10.121.150/viscosity?useUnicode=true&characterEncoding=utf-8" -username root -password 123456 -table liv_pl_top100_10 -export-dir /viscosity/pl_zb_top100/liv/10/pl-r-00000 --input-fields-terminated-by '|'
```
```
sqoop export -connect "jdbc:mysql://10.10.121.150/viscosity?useUnicode=true&characterEncoding=utf-8" -username root -password 123456 -table liv_zb_top100_10 -export-dir /viscosity/pl_zb_top100/liv/10/zb-r-00000 --input-fields-terminated-by '|'
```
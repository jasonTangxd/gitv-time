## 载入ORC类型表
```
CREATE EXTERNAL TABLE IF NOT EXISTS orc_vod_viscosity_user
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
click_num BIGINT,
playlength BIGINT,
timelength BIGINT)
PARTITIONED BY(yyyy string,mm String,dd String)
STORED AS ORC;
```

```
alter table orc_vod_viscosity_user set serdeproperties('serialization.null.format' = '');
```

``后续类型转换启动脚本参考text2orc.sh``

转换后文件大小

```
3.5 G  /user/hive/warehouse/test.db/orc_vod_viscosity_user
```


## HIVE测试
### COUNT(*)
select count(*) from orc_vod_viscosity_user
```
461203874
Time taken: 23.515 seconds, Fetched: 1 row(s)
```
### GROUPBY MAX
select chnname,max(playlength) from orc_vod_viscosity_user group by partner,srcname,chnname
```
Time taken: 43.661 seconds, Fetched: 1428 row(s)
```

## SPARK测试
spark-sql-yarn.sh 5 8
### COUNT(*)
select count(*) from orc_vod_viscosity_user;
```
461203874
Time taken: 57.821 seconds, Fetched 1 row(s)
```

### GROUPBY MAX
select chnname,max(playlength) from orc_vod_viscosity_user group by partner,srcname,chnname ;
```
Time taken: 73.881 seconds, Fetched 1428 row(s)
```
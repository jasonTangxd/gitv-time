## 载入Parquet类型表
```
CREATE EXTERNAL TABLE IF NOT EXISTS parquet_vod_viscosity_user_other
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
STORED AS PARQUET;
```

``后续类型转换启动脚本参考text2parquet.sh``

转换后大小
```
13.7 G  /user/hive/warehouse/test.db/parquet_vod_viscosity_user_other
```


## HIVE测试
### COUNT(*)
select count(*) from parquet_vod_viscosity_user_other
```
461203874
Time taken: 20.537 seconds, Fetched: 1 row(s)
```
### GROUPBY MAX
select chnname,max(playlength) from parquet_vod_viscosity_user_other group by partner,srcname,chnname
```
Time taken: 29.37 seconds, Fetched: 1428 row(s)
```

## SPARK测试
spark-sql-yarn.sh 5 8
### COUNT(*)
select count(*) from parquet_vod_viscosity_user_other;
```
461203874
Time taken: 8.533 seconds, Fetched 1 row(s)
```

### GROUPBY MAX
select chnname,max(playlength) from parquet_vod_viscosity_user_other group by partner,srcname,chnname ;
```
Time taken: 18.704 seconds, Fetched 1428 row(s)
```
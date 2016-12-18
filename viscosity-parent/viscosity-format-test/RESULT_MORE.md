## SIZE OF BD
```
48.1 G  /viscosity/avro/vod/vod_viscosity_user
```

```
61.3 G  /viscosity/seq/vod/vod_viscosity_user
```

```
50.6 G  /viscosity/text/vod/vod_viscosity_user
```

```
15.2 G  /viscosity/parquet/vod/vod_viscosity_user
```

## HIVE测试
### COUNT(*)

``[text]``

select count(*) from text_vod_viscosity_user;
```
460973855
Time taken: 95.304 seconds, Fetched: 1 row(s)
```
``[seq]``

select count(*) from seq_vod_viscosity_user;
```
460973862
Time taken: 201.625 seconds, Fetched: 1 row(s)
```
``[avro]``

select count(*) from avro_vod_viscosity_user;
```
460973855
Time taken: 186.568 seconds, Fetched: 1 row(s)
```
``[parquet]``

select count(*) from parquet_vod_viscosity_user
```
460973857
Time taken: 19.187 seconds, Fetched: 1 row(s)
```

### GROUPBY MAX
``[text]``

select chnname,max(cast(playlength as BIGINT)) from text_vod_viscosity_user group by partner,srcname,chnname ;
```
Time taken: 124.45 seconds, Fetched: 1428 row(s)
```
``[seq]``

select chnname,max(cast(playlength as BIGINT)) from seq_vod_viscosity_user group by partner,srcname,chnname ;
```
Time taken: 240.625 seconds, Fetched: 1428 row(s)
```
``[avro]``

select chnname,max(cast(playlength as BIGINT)) from avro_vod_viscosity_user group by partner,srcname,chnname ;
```
TTime taken: 129.284 seconds, Fetched: 1428 row(s)
```
``[parquet]``

select chnname,max(playlength) from parquet_vod_viscosity_user group by partner,srcname,chnname ;
```
Time taken: 31.284 seconds, Fetched: 1428 row(s)
```

## SPARK测试
spark-sql-yarn.sh 5 8
### COUNT(*)
``[text]``

select count(*) from text_vod_viscosity_user;
```
460973855
Time taken: 88.405 seconds, Fetched 1 row(s)
```
``[seq]``

select count(*) from seq_vod_viscosity_user;
```
460973862
Time taken: 556.833 seconds, Fetched 1 row(s)
```
``[avro]``

select count(*) from avro_vod_viscosity_user;
```
461203874
Time taken: 354.294 seconds, Fetched 1 row(s)
```
``[parquet]``

select count(*) from parquet_vod_viscosity_user;
```
461203876
Time taken: 6.687 seconds, Fetched 1 row(s)
```


### GROUPBY MAX
``[text]``

select chnname,max(cast(playlength as BIGINT)) from text_vod_viscosity_user group by partner,srcname,chnname ;
```
Time taken: 236.502 seconds, Fetched 1428 row(s)
```
``[seq]``

select chnname,max(cast(playlength as BIGINT)) from seq_vod_viscosity_user group by partner,srcname,chnname ;
```
Time taken: 560.08 seconds, Fetched 1428 row(s)
```
``[avro]``

select chnname,max(cast(playlength as BIGINT)) from avro_vod_viscosity_user group by partner,srcname,chnname ;
```
Time taken: 444.703 seconds, Fetched 1428 row(s)
```
``[parquet]``

select chnname,max(playlength) from parquet_vod_viscosity_user group by partner,srcname,chnname ;
```
Time taken: 38.999 seconds, Fetched 1428 row(s)
```

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
后续类型转换启动脚本参考Text2Parquet.sh
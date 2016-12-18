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
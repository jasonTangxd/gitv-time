# 四种文件格式的简单测试
## HDFS的简单写操作
**开始比较唐突的直接使用了textfile,后来考虑到线上全是sequencefile,最后决定把avro parquet加进去直接测试下这四个格式
无压缩大小比较:**
```
3.5 G
(3730403673)

/viscosity/avro/vod/vod_viscosity_user/2016/12/11/cth_file.avro
```

```
3.7 G
(3919391556)

/viscosity/text/vod/vod_viscosity_user/2016/12/11/cth_file.text
```

```
1.1 G
(1180527857)

/viscosity/parquet/vod/vod_viscosity_user/2016/12/11/cth_file.parquet
```

```
4.4 G
(4755179060)

/viscosity/seq/vod/vod_viscosity_user/2016/12/11/cth_file.seq
```
---
* liv_viscosity_program
>
```
writeParquet: cass_table [liv_viscosity_program],use time : 5982ms
writeText: cass_table [liv_viscosity_program],use time : 9377ms
writeSeq: cass_table [liv_viscosity_program],use time : 11191ms
writeAvro: cass_table [liv_viscosity_program],use time : 13535ms
```

* liv_viscosity_user
>
```
writeText: cass_table [liv_viscosity_user],use time : 43642ms
writeAvro: cass_table [liv_viscosity_user],use time : 44692ms
writeParquet: cass_table [liv_viscosity_user],use time : 47578ms
writeSeq: cass_table [liv_viscosity_user],use time : 74837ms
```

* vod_viscosity_program
>
```
writeAvro: cass_table [vod_viscosity_program],use time : 100595ms
writeText: cass_table [vod_viscosity_program],use time : 101419ms
writeParquet: cass_table [vod_viscosity_program],use time : 114136ms
writeSeq: cass_table [vod_viscosity_program],use time : 158861ms
```

* vod_viscosity_user
>
```
writeText: cass_table [vod_viscosity_user],use time : 671821ms
writeAvro: cass_table [vod_viscosity_user],use time : 696363ms
writeParquet: cass_table [vod_viscosity_user],use time : 806913ms
writeSeq: cass_table [vod_viscosity_user],use time : 1213510ms
```

---
## HIVE简单查询
### 建表
```
USE TEST;
```
* TEXTFILE
```
CREATE EXTERNAL TABLE IF NOT EXISTS text_vod_viscosity_user
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
STORED AS TEXTFILE;
```
```
ALTER TABLE text_vod_viscosity_user ADD IF NOT EXISTS PARTITION(yyyy='2016',mm='12',dd='07') location '/viscosity/text/vod/vod_viscosity_user/2016/12/07';
ALTER TABLE text_vod_viscosity_user ADD IF NOT EXISTS PARTITION(yyyy='2016',mm='12',dd='11') location '/viscosity/text/vod/vod_viscosity_user/2016/12/11';
```
* SEQUENCEFILE
```
CREATE EXTERNAL TABLE IF NOT EXISTS seq_vod_viscosity_user
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
```
ALTER TABLE seq_vod_viscosity_user ADD IF NOT EXISTS PARTITION(yyyy='2016',mm='12',dd='07') location '/viscosity/seq/vod/vod_viscosity_user/2016/12/07';
ALTER TABLE seq_vod_viscosity_user ADD IF NOT EXISTS PARTITION(yyyy='2016',mm='12',dd='11') location '/viscosity/seq/vod/vod_viscosity_user/2016/12/11';
```
* AVROFILE
```
CREATE EXTERNAL TABLE avro_vod_viscosity_user
PARTITIONED BY(yyyy string,mm String,dd String)
ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.avro.AvroSerDe'
STORED AS
INPUTFORMAT  'org.apache.hadoop.hive.ql.io.avro.AvroContainerInputFormat'
OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.avro.AvroContainerOutputFormat'
TBLPROPERTIES ('avro.schema.literal'='{
                                    "namespace": "cn.gitv.bi.viscosity.avrodemo",
                                    "type": "record",
                                    "name": "VodUser",
                                    "fields": [
                                      {
                                        "name": "partner",
                                        "type": "string"
                                      },
                                      {
                                        "name": "logdate",
                                        "type": "string"
                                      },
                                      {
                                        "name": "mac",
                                        "type": "string"
                                      },
                                      {
                                        "name": "srcname",
                                        "type": "string"
                                      },
                                      {
                                        "name": "chnname",
                                        "type": "string"
                                      },
                                      {
                                        "name": "chnId",
                                        "type": "string"
                                      },
                                      {
                                        "name": "albumname",
                                        "type": "string"
                                      },
                                      {
                                        "name": "albumId",
                                        "type": "string"
                                      },
                                      {
                                        "name": "playorder",
                                        "type": "string"
                                      },
                                      {
                                        "name": "videoId",
                                        "type": "string"
                                      },
                                      {
                                        "name": "click_num",
                                        "type": "long"
                                      },
                                      {
                                        "name": "playlength",
                                        "type": "long"
                                      },
                                      {
                                        "name": "timelength",
                                        "type": "long"
                                      }
                                    ]
                                  }');
```
```
ALTER TABLE avro_vod_viscosity_user ADD IF NOT EXISTS PARTITION(yyyy='2016',mm='12',dd='07') location '/viscosity/avro/vod/vod_viscosity_user/2016/12/07';
ALTER TABLE avro_vod_viscosity_user ADD IF NOT EXISTS PARTITION(yyyy='2016',mm='12',dd='11') location '/viscosity/avro/vod/vod_viscosity_user/2016/12/11';
```
* PARQUETFILE
```
CREATE EXTERNAL TABLE IF NOT EXISTS parquet_vod_viscosity_user
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
```
ALTER TABLE parquet_vod_viscosity_user ADD IF NOT EXISTS PARTITION(yyyy='2016',mm='12',dd='11') location '/viscosity/parquet/vod/vod_viscosity_user/2016/12/11';
```

## HIVE测试
### COUNT(*)

``[text]``
select count(*) from text_vod_viscosity_user;
```
33250363
Time taken: 17.048 seconds, Fetched: 1 row(s)
```
``[seq]``
select count(*) from seq_vod_viscosity_user;
```
33250363
Time taken: 22.237 seconds, Fetched: 1 row(s)
```
``[avro]``
select count(*) from avro_vod_viscosity_user;
```
33250363
Time taken: 25.019 seconds, Fetched: 1 row(s)
```
``[parquet]``
select count(*) from parquet_vod_viscosity_user
```
33250363
Time taken: 17.189 seconds, Fetched: 1 row(s)
```

### GROUPBY MAX
``[text]``
select chnname,max(cast(playlength as BIGINT)) from text_vod_viscosity_user group by partner,srcname,chnname ;
```
Time taken: 21.179 seconds, Fetched: 1159 row(s)
```
``[seq]``
select chnname,max(cast(playlength as BIGINT)) from seq_vod_viscosity_user group by partner,srcname,chnname ;
```
Time taken: 26.24 seconds, Fetched: 1159 row(s)
```
``[avro]``
select chnname,max(cast(playlength as BIGINT)) from avro_vod_viscosity_user group by partner,srcname,chnname ;
```
Time taken: 25.273 seconds, Fetched: 1159 row(s)
```
``[parquet]``
select chnname,max(playlength) from parquet_vod_viscosity_user group by partner,srcname,chnname ;
```
Time taken: 23.491 seconds, Fetched: 1159 row(s)
```
```
2016-12-12 19:50:48 警告: parquet.hadoop.ParquetRecordReader: Can not initialize counter due to context is not a instance of TaskInputOutputContext, but is org.apache.hadoop.mapreduce.task.TaskAttemptContextImpl
2016-12-12 19:50:48 信息: parquet.hadoop.InternalParquetRecordReader: RecordReader initialized will read a total of 7326172 records.
2016-12-12 19:50:48 信息: parquet.hadoop.InternalParquetRecordReader: at row 0. reading next block
2016-12-12 19:50:51 信息: parquet.hadoop.InternalParquetRecordReader: block read in memory in 3038 ms. row count = 3586072
2016-12-12 19:53:00 警告: parquet.hadoop.ParquetRecordReader: Can not initialize counter due to context is not a instance of TaskInputOutputContext, but is org.apache.hadoop.mapreduce.task.TaskAttemptContextImpl
2016-12-12 19:53:00 信息: parquet.hadoop.InternalParquetRecordReader: RecordReader initialized will read a total of 7326172 records.
2016-12-12 19:53:00 信息: parquet.hadoop.InternalParquetRecordReader: at row 0. reading next block
2016-12-12 19:53:02 信息: parquet.hadoop.InternalParquetRecordReader: block read in memory in 1765 ms. row count = 3586072
```

## SPARK测试
spark-sql-yarn.sh 5 8
### COUNT(*)
``[text]``
select count(*) from text_vod_viscosity_user;
```
33250363
Time taken: 5.146 seconds, Fetched 1 row(s)
```
``[seq]``
select count(*) from seq_vod_viscosity_user;
```
33250363
Time taken: 22.012 seconds, Fetched 1 row(s)
```
``[avro]``
select count(*) from avro_vod_viscosity_user;
```
33250363
Time taken: 15.687 seconds, Fetched 1 row(s)
```
``[parquet]``
select count(*) from parquet_vod_viscosity_user;
```
33250363
Time taken: 5.582 seconds, Fetched 1 row(s)
```


### GROUPBY MAX
``[text]``
select chnname,max(cast(playlength as BIGINT)) from text_vod_viscosity_user group by partner,srcname,chnname ;
```
Time taken: 15.761 seconds, Fetched 1159 row(s)
```
``[seq]``
select chnname,max(cast(playlength as BIGINT)) from seq_vod_viscosity_user group by partner,srcname,chnname ;
```
Time taken: 30.253 seconds, Fetched 1159 row(s)
```
``[avro]``
select chnname,max(cast(playlength as BIGINT)) from avro_vod_viscosity_user group by partner,srcname,chnname ;
```
Time taken: 21.282 seconds, Fetched 1159 row(s)
```
``[parquet]``
select chnname,max(playlength) from parquet_vod_viscosity_user group by partner,srcname,chnname ;
```
Time taken: 5.163 seconds, Fetched 1159 row(s)
```
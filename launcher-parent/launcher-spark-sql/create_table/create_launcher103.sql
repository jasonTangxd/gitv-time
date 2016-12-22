create external table launcher_log_103
(Action STRING,
P STRING,
MC STRING,
V STRING,
UID STRING,
STBID STRING,
TS STRING,
OS STRING,
HM STRING,
initType STRING,
initData STRING,
albumId STRING,
chnId STRING,
cpId STRING,
type STRING,
cpContentId STRING,
topicLayout STRING,
topicPicBg STRING,
chnName STRING,
currTypingCode STRING,
currTypingName STRING,
typings STRING,
num STRING,
tvId STRING,
tagId STRING,
pageName STRING,
screenInfo STRING,
picUrl STRING,
TP STRING,
RE STRING,
UG STRING,
CTP STRING,
ip STRING,
tmstamp STRING,
record_day STRING
)
PARTITIONED BY(yymmdd String)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '|'
STORED AS TEXTFILE;

ALTER TABLE launcher_log_103 ADD IF NOT EXISTS PARTITION(yymmdd='2016-12-20') location '/launcher/launcher-log-103/2016-12-20';

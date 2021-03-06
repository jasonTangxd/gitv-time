create external table launcher_log_102
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
SID STRING,
AR STRING,
X STRING,
Y STRING,
TNUM STRING,
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

ALTER TABLE launcher_log_102 ADD IF NOT EXISTS PARTITION(yymmdd='2016-12-24') location '/launcher/launcher-log-102/2016-12-24';
ALTER TABLE launcher_log_102 ADD IF NOT EXISTS PARTITION(yymmdd='2016-12-25') location '/launcher/launcher-log-102/2016-12-25';
ALTER TABLE launcher_log_102 ADD IF NOT EXISTS PARTITION(yymmdd='2016-12-26') location '/launcher/launcher-log-102/2016-12-26';
ALTER TABLE launcher_log_102 ADD IF NOT EXISTS PARTITION(yymmdd='2016-12-27') location '/launcher/launcher-log-102/2016-12-27';
ALTER TABLE launcher_log_102 ADD IF NOT EXISTS PARTITION(yymmdd='2016-12-28') location '/launcher/launcher-log-102/2016-12-28';

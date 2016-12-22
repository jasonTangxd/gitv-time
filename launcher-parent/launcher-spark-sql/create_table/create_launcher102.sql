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

ALTER TABLE launcher_log_102 ADD IF NOT EXISTS PARTITION(yymmdd='2016-12-16') location '/launcher/launcher-log-102/2016-12-16';
ALTER TABLE launcher_log_102 ADD IF NOT EXISTS PARTITION(yymmdd='2016-12-17') location '/launcher/launcher-log-102/2016-12-17';
ALTER TABLE launcher_log_102 ADD IF NOT EXISTS PARTITION(yymmdd='2016-12-18') location '/launcher/launcher-log-102/2016-12-18';
ALTER TABLE launcher_log_102 ADD IF NOT EXISTS PARTITION(yymmdd='2016-12-19') location '/launcher/launcher-log-102/2016-12-19';
ALTER TABLE launcher_log_102 ADD IF NOT EXISTS PARTITION(yymmdd='2016-12-20') location '/launcher/launcher-log-102/2016-12-20';
ALTER TABLE launcher_log_102 ADD IF NOT EXISTS PARTITION(yymmdd='2016-12-21') location '/launcher/launcher-log-102/2016-12-21';

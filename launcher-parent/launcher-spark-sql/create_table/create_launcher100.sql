create external table launcher_log_100
(Action STRING,
P STRING,
MC STRING,
V STRING,
UID STRING,
STBID STRING,
TS STRING,
OS STRING,
HM STRING,
UG STRING,
LIC STRING,
ip STRING,
tmstamp STRING,
record_day STRING
)
PARTITIONED BY(yymmdd String)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '|'
STORED AS TEXTFILE;

ALTER TABLE launcher_log_100 ADD IF NOT EXISTS PARTITION(yymmdd='2016-12-15') location '/launcher/launcher-log-100/2016-12-15';
ALTER TABLE launcher_log_100 ADD IF NOT EXISTS PARTITION(yymmdd='2016-12-16') location '/launcher/launcher-log-100/2016-12-16';
ALTER TABLE launcher_log_100 ADD IF NOT EXISTS PARTITION(yymmdd='2016-12-17') location '/launcher/launcher-log-100/2016-12-17';
ALTER TABLE launcher_log_100 ADD IF NOT EXISTS PARTITION(yymmdd='2016-12-18') location '/launcher/launcher-log-100/2016-12-18';
ALTER TABLE launcher_log_100 ADD IF NOT EXISTS PARTITION(yymmdd='2016-12-19') location '/launcher/launcher-log-100/2016-12-19';
ALTER TABLE launcher_log_100 ADD IF NOT EXISTS PARTITION(yymmdd='2016-12-20') location '/launcher/launcher-log-100/2016-12-20';
ALTER TABLE launcher_log_100 ADD IF NOT EXISTS PARTITION(yymmdd='2016-12-21') location '/launcher/launcher-log-100/2016-12-21';
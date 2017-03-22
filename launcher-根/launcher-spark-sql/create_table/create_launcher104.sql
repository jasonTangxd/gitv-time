create external table launcher_log_104
(Action STRING,
P STRING,
MC STRING,
V STRING,
UID STRING,
STBID STRING,
TS STRING,
OS STRING,
HM STRING,
TNUM STRING,
OTN STRING,
UG STRING,
ip STRING,
tmstamp STRING,
record_day STRING
)
PARTITIONED BY(yymmdd String)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '|'
STORED AS TEXTFILE;

ALTER TABLE launcher_log_104 ADD IF NOT EXISTS PARTITION(yymmdd='2016-12-24') location '/launcher/launcher-log-104/2016-12-24';
ALTER TABLE launcher_log_104 ADD IF NOT EXISTS PARTITION(yymmdd='2016-12-25') location '/launcher/launcher-log-104/2016-12-25';
ALTER TABLE launcher_log_104 ADD IF NOT EXISTS PARTITION(yymmdd='2016-12-26') location '/launcher/launcher-log-104/2016-12-26';
ALTER TABLE launcher_log_104 ADD IF NOT EXISTS PARTITION(yymmdd='2016-12-27') location '/launcher/launcher-log-104/2016-12-27';
ALTER TABLE launcher_log_104 ADD IF NOT EXISTS PARTITION(yymmdd='2016-12-28') location '/launcher/launcher-log-104/2016-12-28';
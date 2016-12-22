create external table launcher_log_101
(Action STRING,
P STRING,
MC STRING,
V STRING,
UID STRING,
STBID STRING,
TS STRING,
OS STRING,
HM STRING,
OV STRING,
UG STRING,
ip STRING,
tmstamp STRING,
record_day STRING
)
PARTITIONED BY(yymmdd String)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '|'
STORED AS TEXTFILE;
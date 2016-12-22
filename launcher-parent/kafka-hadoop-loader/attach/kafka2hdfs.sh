#!/bin/bash
. /etc/bashrc
today=`date -d '-1 day' +%Y-%m-%d`
hadoop jar kafka-hadoop-loader.jar -c off -g khloader100 -o earliest -t launcher-log-100 -z 'slave7.gitv.rack2.bk:2281,slave8.gitv.rack2.bk:2281,slave9.gitv.rack2.bk:2281,slave10.gitv.rack2.bk:2281,slave11.gitv.rack2.bk:2281,master2.gitv.rack2.bk:2281,master1.gitv.rack1.bk:2281,slave1.gitv.rack1.bk:2281,slave2.gitv.rack1.bk:2281,slave3.gitv.rack1.bk:2281,slave4.gitv.rack1.bk:2281' /launcher
hadoop jar kafka-hadoop-loader.jar -c off -g khloader101 -o earliest -t launcher-log-101 -z 'slave7.gitv.rack2.bk:2281,slave8.gitv.rack2.bk:2281,slave9.gitv.rack2.bk:2281,slave10.gitv.rack2.bk:2281,slave11.gitv.rack2.bk:2281,master2.gitv.rack2.bk:2281,master1.gitv.rack1.bk:2281,slave1.gitv.rack1.bk:2281,slave2.gitv.rack1.bk:2281,slave3.gitv.rack1.bk:2281,slave4.gitv.rack1.bk:2281' /launcher
hadoop jar kafka-hadoop-loader.jar -c off -g khloader102 -o earliest -t launcher-log-102 -z 'slave7.gitv.rack2.bk:2281,slave8.gitv.rack2.bk:2281,slave9.gitv.rack2.bk:2281,slave10.gitv.rack2.bk:2281,slave11.gitv.rack2.bk:2281,master2.gitv.rack2.bk:2281,master1.gitv.rack1.bk:2281,slave1.gitv.rack1.bk:2281,slave2.gitv.rack1.bk:2281,slave3.gitv.rack1.bk:2281,slave4.gitv.rack1.bk:2281' /launcher
hadoop jar kafka-hadoop-loader.jar -c off -g khloader103 -o earliest -t launcher-log-103 -z 'slave7.gitv.rack2.bk:2281,slave8.gitv.rack2.bk:2281,slave9.gitv.rack2.bk:2281,slave10.gitv.rack2.bk:2281,slave11.gitv.rack2.bk:2281,master2.gitv.rack2.bk:2281,master1.gitv.rack1.bk:2281,slave1.gitv.rack1.bk:2281,slave2.gitv.rack1.bk:2281,slave3.gitv.rack1.bk:2281,slave4.gitv.rack1.bk:2281' /launcher
hadoop jar kafka-hadoop-loader.jar -c off -g khloader104 -o earliest -t launcher-log-104 -z 'slave7.gitv.rack2.bk:2281,slave8.gitv.rack2.bk:2281,slave9.gitv.rack2.bk:2281,slave10.gitv.rack2.bk:2281,slave11.gitv.rack2.bk:2281,master2.gitv.rack2.bk:2281,master1.gitv.rack1.bk:2281,slave1.gitv.rack1.bk:2281,slave2.gitv.rack1.bk:2281,slave3.gitv.rack1.bk:2281,slave4.gitv.rack1.bk:2281' /launcher
hive <<EOF
use launcher;
ALTER TABLE launcher_log_100 ADD IF NOT EXISTS PARTITION(yymmdd='${today}') location '/launcher/launcher-log-100/${today}';
ALTER TABLE launcher_log_101 ADD IF NOT EXISTS PARTITION(yymmdd='${today}') location '/launcher/launcher-log-101/${today}';
ALTER TABLE launcher_log_102 ADD IF NOT EXISTS PARTITION(yymmdd='${today}') location '/launcher/launcher-log-102/${today}';
ALTER TABLE launcher_log_103 ADD IF NOT EXISTS PARTITION(yymmdd='${today}') location '/launcher/launcher-log-103/${today}';
ALTER TABLE launcher_log_104 ADD IF NOT EXISTS PARTITION(yymmdd='${today}') location '/launcher/launcher-log-104/${today}';
EOF
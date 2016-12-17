#!/bin/bash
#init var
TIMESTAMP=`date -d "5 min ago" +%Y%m%d%H%M`
YEAR=`date "+%Y"`
MONTH=`date "+%m"`
DAY=`date "+%d"`
nginx_pid=`cat /data/nginx/logs/nginx.pid`
bakup_dir='/data1/bi/launcher/'
log_dir='/data/pingback/logs/'
launcher_file='launcher_pb-access.log'
mv_log="launcher_pb-${TIMESTAMP}.log"
mv_gzlog="${mv_log}.gz"
#cut log
cd ${log_dir}
mv ${launcher_file} ${mv_log}
kill -s USR1 $nginx_pid
#mkdir log tar dir
move_dir=${bakup_dir}${YEAR}/${MONTH}/${DAY}
[ ! -d ${move_dir} ] && mkdir -p ${move_dir}
#log to gzip
cat launcher_pb-${TIMESTAMP}.log|gzip > ${move_dir}/${mv_gzlog}
rm -rf ${mv_log}

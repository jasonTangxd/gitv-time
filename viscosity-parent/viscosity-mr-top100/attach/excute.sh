#!/bin/bash
. /etc/profile
#screen_day=$1
screen_day=30
today=`date +%Y-%m-%d`
excute_day=`date +%Y%m%d`
delete_day=`date -d "-50 day" +%Y%m%d`
for i in `seq 1 ${screen_day}`
do
day=`date -d "-${i} day" +%Y/%m/%d`
days[${i}]=${day}
done
days_num=${#days[@]}
vom_need_days=${days[@]:0:$days_num}

hadoop jar /data/bash/viscosity/vom.jar vod "${vom_need_days}" "${today}"
hadoop jar /data/bash/viscosity/vom.jar liv "${vom_need_days}" "${today}"

mysql -h'10.10.121.150' -u'root' -p'123456' -e "
use viscosity;
create table IF NOT EXISTS vod_pl_top100_${excute_day}(partner varchar(256),chnid int,albumname varchar(256),albumid varchar(256),pl int,timelength int);
create table IF NOT EXISTS vod_zb_top100_${excute_day}(partner varchar(256),chnid int,albumname varchar(256),albumid varchar(256),zb double,timelength int);
create table IF NOT EXISTS liv_pl_top100_${excute_day}(partner varchar(256),chncode varchar(256),albumname varchar(256),albumid varchar(256),pl int,timelength int);
create table IF NOT EXISTS liv_zb_top100_${excute_day}(partner varchar(256),chncode varchar(256),albumname varchar(256),albumid varchar(256),zb double,timelength int);
ALTER TABLE vod_pl_top100_${excute_day} CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE vod_zb_top100_${excute_day} CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE liv_pl_top100_${excute_day} CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE liv_zb_top100_${excute_day} CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
drop table if exists vod_pl_top100_${delete_day};
drop table if exists vod_zb_top100_${delete_day};
drop table if exists liv_pl_top100_${delete_day};
drop table if exists liv_zb_top100_${delete_day};
"
sqoop export -connect "jdbc:mysql://10.10.121.150/viscosity?useUnicode=true&characterEncoding=utf-8" -username root -password 123456 -table vod_pl_top100_${excute_day} -export-dir /viscosity/pl_zb_top100/${today}/vod/pl/pl-r-00000 --input-fields-terminated-by '|'
sqoop export -connect "jdbc:mysql://10.10.121.150/viscosity?useUnicode=true&characterEncoding=utf-8" -username root -password 123456 -table vod_zb_top100_${excute_day} -export-dir /viscosity/pl_zb_top100/${today}/vod/zb/zb-r-00000 --input-fields-terminated-by '|'
sqoop export -connect "jdbc:mysql://10.10.121.150/viscosity?useUnicode=true&characterEncoding=utf-8" -username root -password 123456 -table liv_pl_top100_${excute_day} -export-dir /viscosity/pl_zb_top100/${today}/liv/pl/pl-r-00000 --input-fields-terminated-by '|'
sqoop export -connect "jdbc:mysql://10.10.121.150/viscosity?useUnicode=true&characterEncoding=utf-8" -username root -password 123456 -table liv_zb_top100_${excute_day} -export-dir /viscosity/pl_zb_top100/${today}/liv/zb/zb-r-00000 --input-fields-terminated-by '|'
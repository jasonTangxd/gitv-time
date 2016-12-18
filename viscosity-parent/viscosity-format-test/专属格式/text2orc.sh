#!/bin/bash
. /etc/profile
for i in `seq 1 23`
do
day=`date -d "-${i} day" +%Y-%m-%d`
days[${i}]=${day}
done

days_num=${#days[@]}
vom_need_days=${days[@]:0:$days_num}
for item in ${vom_need_days}
do
yyyy=`echo ${item}|cut -d "-" -f1`
mm=`echo ${item}|cut -d "-" -f2`
dd=`echo ${item}|cut -d "-" -f3`
hive <<EOF
use test;
ALTER TABLE orc_vod_viscosity_user ADD IF NOT EXISTS PARTITION(yyyy='${yyyy}',mm='${mm}',dd='${dd}');
INSERT OVERWRITE TABLE orc_vod_viscosity_user partition(yyyy='${yyyy}',mm='${mm}',dd='${dd}')
select partner,logdate,mac,srcname,chnname,chnId,albumname,albumId,playorder,videoId,click_num,playlength,timelength from text_vod_viscosity_user where yyyy='${yyyy}' and mm='${mm}' and dd='${dd}';
EOF
done
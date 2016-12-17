#!/bin/bash
. /etc/bashrc
today=`date +%Y-%m-%d`
yestoday=`date -d '-1 day' +%Y-%m-%d`
qt=`date -d '-2 day' +%Y-%m-%d`
#today='2016-12-10'
#yestoday='2016-12-09'
#qt='2016-12-08'
yyyy=`echo ${yestoday}|cut -d "-" -f1`
mm=`echo ${yestoday}|cut -d "-" -f2`
dd=`echo ${yestoday}|cut -d "-" -f3`
nohup java -jar /data/opt/script/viscosity/vtoh.jar ${qt} ${today} 'vod' 'vod_viscosity_user' ${yestoday} 'text' >> nohup.out 2>&1 &
nohup java -jar /data/opt/script/viscosity/vtoh.jar ${qt} ${today} 'vod' 'vod_viscosity_program' ${yestoday} 'text' >> nohup.out 2>&1 &
nohup java -jar /data/opt/script/viscosity/vtoh.jar ${qt} ${today} 'liv' 'liv_viscosity_user' ${yestoday} 'text' >> nohup.out 2>&1 &
nohup java -jar /data/opt/script/viscosity/vtoh.jar ${qt} ${today} 'liv' 'liv_viscosity_program' ${yestoday} 'text' >> nohup.out 2>&1 &
#-----------------------
nohup java -jar /data/opt/script/viscosity/vtoh.jar ${qt} ${today} 'vod' 'vod_viscosity_user' ${yestoday} 'seq' >> nohup.out 2>&1 &
nohup java -jar /data/opt/script/viscosity/vtoh.jar ${qt} ${today} 'vod' 'vod_viscosity_program' ${yestoday} 'seq' >> nohup.out 2>&1 &
nohup java -jar /data/opt/script/viscosity/vtoh.jar ${qt} ${today} 'liv' 'liv_viscosity_user' ${yestoday} 'seq' >> nohup.out 2>&1 &
nohup java -jar /data/opt/script/viscosity/vtoh.jar ${qt} ${today} 'liv' 'liv_viscosity_program' ${yestoday} 'seq' >> nohup.out 2>&1 &
#-----------------------
nohup java -jar /data/opt/script/viscosity/vtoh.jar ${qt} ${today} 'vod' 'vod_viscosity_user' ${yestoday} 'avro' >> nohup.out 2>&1 &
nohup java -jar /data/opt/script/viscosity/vtoh.jar ${qt} ${today} 'vod' 'vod_viscosity_program' ${yestoday} 'avro' >> nohup.out 2>&1 &
nohup java -jar /data/opt/script/viscosity/vtoh.jar ${qt} ${today} 'liv' 'liv_viscosity_user' ${yestoday} 'avro' >> nohup.out 2>&1 &
nohup java -jar /data/opt/script/viscosity/vtoh.jar ${qt} ${today} 'liv' 'liv_viscosity_program' ${yestoday} 'avro' >> nohup.out 2>&1 &
#------------------------
nohup java -jar /data/opt/script/viscosity/vtoh.jar ${qt} ${today} 'vod' 'vod_viscosity_user' ${yestoday} 'parquet' >> nohup.out 2>&1 &
nohup java -jar /data/opt/script/viscosity/vtoh.jar ${qt} ${today} 'vod' 'vod_viscosity_program' ${yestoday} 'parquet' >> nohup.out 2>&1 &
nohup java -jar /data/opt/script/viscosity/vtoh.jar ${qt} ${today} 'liv' 'liv_viscosity_user' ${yestoday} 'parquet' >> nohup.out 2>&1 &
nohup java -jar /data/opt/script/viscosity/vtoh.jar ${qt} ${today} 'liv' 'liv_viscosity_program' ${yestoday} 'parquet' >> nohup.out 2>&1 &
#注意！无论分区location的路径是否存在，都可以制定这个分区
hive <<EOF
use test;
ALTER TABLE avro_vod_viscosity_user ADD IF NOT EXISTS PARTITION(yyyy='${yyyy}',mm='${mm}',dd='${dd}') location '/viscosity/avro/vod/vod_viscosity_user/${yyyy}/${mm}/${dd}';
ALTER TABLE seq_vod_viscosity_user ADD IF NOT EXISTS PARTITION(yyyy='${yyyy}',mm='${mm}',dd='${dd}') location '/viscosity/seq/vod/vod_viscosity_user/${yyyy}/${mm}/${dd}';
ALTER TABLE text_vod_viscosity_user ADD IF NOT EXISTS PARTITION(yyyy='${yyyy}',mm='${mm}',dd='${dd}') location '/viscosity/text/vod/vod_viscosity_user/${yyyy}/${mm}/${dd}';
ALTER TABLE parquet_vod_viscosity_user ADD IF NOT EXISTS PARTITION(yyyy='${yyyy}',mm='${mm}',dd='${dd}') location '/viscosity/parquet/vod/vod_viscosity_user/${yyyy}/${mm}/${dd}';
EOF
#!/bin/bash
. /etc/profile
set -x
set -e
work_path='/root/user_info/update_part/'
# mysql -h'10.25.130.108' -u'pingback' -p'gitv_pingback'||mysql -h'10.25.130.114' -u'pingback' -p'bUZ1-hVy'||mysql -h'10.55.130.199' -u'gitv_rd' -p'1234.gitv_rd'||mysql -h'10.57.130.199' -u'gitv_pingback' -p'91b6c0a8bc'  ZJYD
sh_day=`date -d "-1 day" +%Y%m%d`
#it it for ah_cmcc
mysql -h'10.55.130.199' -u'gitv_rd' -p'1234.gitv_rd' -e "
use ibcp;
select CONCAT(partner,',',MAC_ADDR,',',ifnull(USER_TYPE,''),',',ifnull(STATUS,'')) as 'partner,MAC,user_type,status' from
(select 'AH_CMCC' as partner,a.MAC_ADDR,a.USER_TYPE,if(b.STATUS=1,6,b.STATUS) as STATUS from (select MAC_ADDR,USER_TYPE,CHILD_ACCOUNT_ID from cont_user_mac where MAC_ADDR is not null and MAC_ADDR!='')a join cont_user_child b on a.CHILD_ACCOUNT_ID=b.USER_ID) final;
" > ${work_path}ah_cmcc.csv
sed -i '1d' ${work_path}ah_cmcc.csv

#it is for js_cmcc
mysql -h'10.25.130.114' -u'pingback' -p'bUZ1-hVy' -e "
use fiona;
select CONCAT(partner,',',mac,',',ifnull(user_type,''),',',ifnull(statu,'')) as 'partner,MAC,user_type,status' from
(select 'JS_CMCC' as partner,mac,user_type,CASE WHEN status=1 and first_login=1 THEN 1 WHEN status=1 and first_login=0 THEN 6 WHEN status=0 and first_login=0 THEN 4 ELSE null END as statu from hdc_ordering_relation_info where mac is not null and mac!='') final;
" > ${work_path}js_cmcc.csv
sed -i '1d' ${work_path}js_cmcc.csv

#it is for js_cmcc_cp
mysql -h'10.25.130.108' -u'pingback' -p'gitv_pingback' -e "
use fiona;
select CONCAT(partner,',',MAC,',',ifnull(user_type,''),',',ifnull(status,'')) as 'partner,MAC,user_type,status' from
(select 'JS_CMCC_CP' as partner,a.MAC,
CASE WHEN b.idType='01' THEN 1 WHEN b.idType='91' THEN 2 WHEN b.idType='92' THEN 3 ELSE null END as user_type,
CASE WHEN b.opType='0' THEN 1 WHEN b.opType='1' THEN 2 WHEN b.opType='2' THEN 4 WHEN b.opType='3' THEN 5 WHEN b.opType='6' THEN 6 WHEN b.opType='8' THEN 7 ELSE null END as status from (select MAC,USER_ID from active_success where MAC is not null and MAC!='')a join order_relation b on a.USER_ID=b.phoneNumber) final;
" > ${work_path}js_cmcc_cp.csv
sed -i '1d' ${work_path}js_cmcc_cp.csv
#对这三个合作伙伴进行操作
python /data/soft/build_shell/oneday_upcas.py > /dev/null 2>&1 &


#it is for zj,but not need now
# mysql -h'10.53.71.31' -u'pingback' -p'pingback'<<-EOF EOF

# cd /tmp/user_info/
# for file in `ls *.csv`
# do
# 	for item in `cat ${file}`
# 	do
# 		partner=`echo ${item}|awk -F',' '{print $1}'`
# 		mac=`echo ${item}|awk -F',' '{print $2}'`
# 		user_type=`echo ${item}|awk -F',' '{print $3}'`
# 		status=`echo ${item}|awk -F',' '{print $4}'`
# 		res=`cqlsh 10.10.121.119 9042 --request-timeout 1200000 <<-EOF
# 		use likang;
# 		select count(status) from user_info where partner='${partner}' and mac_addr='${mac}';
# 		exit;
# 		EOF`
# 		echo res= ${res}
# 		[[ ${res} =~ '0' ]]&&{
# 	    	#含有0说明不存在这条记录,这条信息不做操作
# 	    	echo 'no this record,pass'
# 	    }||{
# 	    	#做相关更新操作
# 	    	has_status=`cqlsh 10.10.121.119 9042 --request-timeout 120000 <<-EOF
# 			use likang;
# 			select count(*) from user_info where partner='${partner}' and mac_addr='${mac}' and status=${status} ALLOW FILTERING;
# 			exit;
# 			EOF`
# 			echo has_status= ${has_status}
# 			[[ ${has_status} =~ '0' ]]&&{
# 	    		#说明现在的status和库中的不同
# 	    		cqlsh 10.10.121.119 9042 --request-timeout 120000 <<-EOF
# 				use likang;
# 				UPDATE user_info SET status = ${status},status_updatetime='${sh_day}' where partner='${partner}' and mac_addr='${mac}';
# 				exit;
# 				EOF
# 				echo 'updated one record:status and updatetime'
# 			}||echo 'status no need to update'
# 	    	#it will change user_type:
# 	    	cqlsh 10.10.121.119 9042 --request-timeout 120000 <<-EOF
# 				use likang;
# 				UPDATE user_info SET user_type=${user_type} where partner='${partner}' and mac_addr='${mac}';
# 				exit;
# 			EOF
# 			}
# 	done
# 	echo "one file excuted......"
# done

# cqlsh 10.10.121.119 9042 --request-timeout 120000 <<-EOF
# use likang;
# COPY user_info(partner,mac_addr,user_type,status) FROM '/tmp/ah_cmcc.csv' WITH HEADER = true ;
# exit;
# EOF

# cqlsh 10.10.121.119 9042 --request-timeout 120000 <<-EOF
# use likang;
# COPY user_info(partner,mac_addr,user_type,status) FROM '/tmp/js_cmcc.csv' WITH HEADER = true ;
# exit;
# EOF

# cqlsh 10.10.121.119 9042 --request-timeout 120000 <<-EOF
# use likang;
# COPY user_info(partner,mac_addr,user_type,status) FROM '/tmp/js_cmcc_cp.csv' WITH HEADER = true ;
# exit;
# EOF
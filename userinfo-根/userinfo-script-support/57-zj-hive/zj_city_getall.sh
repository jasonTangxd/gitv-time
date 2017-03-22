#!/bin/bash
#it is in 10.10.121.57
. /etc/profile
work_path='/root/user_info/zj_city/'
set -x
set -e
#it is for zj_hive
hive <<EOF
use zj;
insert overwrite local directory '/root/user_info/zj_city/'
row format delimited
fields terminated by ','
select "ZJYD" as partner,mac,city_name,cust_id as user_id from mac_stbid_zjyd_before where city_name != 'None' and mac != 'NULL'
union all
select "ZJYD" as partner,mac,city_name,cust_id as user_id from mac_stbid_zjyd where city_name != 'None' and mac != 'NULL';
EOF

cd ${work_path}
for item in `ls`
do
	cat ${item} >> /root/zj_city.csv
done

cqlsh 10.10.121.138 9042 --request-timeout 1200000 <<-EOF
COPY userinfo.user_info (partner,mac_addr,city_name,user_id) FROM '/root/zj_city.csv';
EOF

rm -rf /root/user_info/zj_city/*
#rm -rf /root/zj_hive.csv

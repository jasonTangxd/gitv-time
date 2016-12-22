#!/bin/bash
#it is in 10.10.121.57
. /etc/profile
work_path='/root/user_info/zj_hive/'
set -x
set -e
#it is for zj_hive
hive <<EOF
use zj;
insert overwrite local directory '${work_path}'
row format delimited
fields terminated by ','
select a.family_id as main_account_id,
a.family_id as child_account_id,
a.family_id as user_id,
a.mac,
'ZJYD' as partner,
a.account_time as account_time
,a.account_time as activate_time,
a.status,'浙江省' as province,
b.city_name,
a.area_name from
(select family_id,
substr(area_id,1,3) as area3_id,
if(length(area_id)=3,'',area_name) as area_name,
concat(substr(open_account_time,1,4),'-',substr(open_account_time,5,2),'-',substr(open_account_time,7,2),' ',substr(open_account_time,9,2),':',substr(open_account_time,11,2),':',substr(open_account_time,13,2)) as account_time,
mac,
case when user_status='F0B' then 7 when user_status='F0A' then 6 when user_status='F0X' then 2 when user_status='F0K' then 4 else 0 end as status
 from stbid_mac_all where mac is not null and mac != 'null')a
join zj_city_code b on a.area3_id=b.city_code;
EOF

cd ${work_path}
for part_file in `ls`
do
	cat ${part_file} >> /root/zj_hive.csv
done

cqlsh 10.10.121.138 9042 --request-timeout 1200000 <<-EOF
COPY userinfo.user_info (main_account_id,child_account_id,user_id,mac_addr,partner,account_time,activate_time,status,province,city_name,area_name) FROM '/root/zj_hive.csv';
EOF

rm -rf /root/user_info/zj_hive/*
rm -rf /root/zj_hive.csv

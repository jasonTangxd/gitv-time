CREATE TABLE user_info (  
main_account_id String,  
child_account_id String,  
user_id String,
mac_addr String, 
partner String,
account_time String,
recode_date String,
activate_time String,
first_open String,
last_open String,
status String,
user_type String,
now_app_vsersion String,
update_history String,
province String,
city_name String,
area_name String)
ROW FORMAT DELIMITED FIELDS TERMINATED BY ','
STORED AS TEXTFILE
LOCATION '/test/user_info';
----------------------
create table test.demo1
as
select if(phonenumber is null,'',phonenumber) as main_account_id,
if(phonenumber is null,'',phonenumber) as child_account_id,
if(phonenumber is null,'',phonenumber) as user_id,
mac as mac_addr,
'JS_CMCC' as partner,
'' as account_time,
'' as activate_time,
case when activation='激活' then '0' when activation='未激活' then '1' else'' end as status,
'' as user_type,
'' as now_app_vsersion,
'' as update_history,
'江苏省' as province,
if(cityname is null,'',cityname) as city_name,
'' as area_name 
from js_sp_user_info where mac!='';
--------------------------------------------
insert into table test.user_info
select m.main_account_id,
m.child_account_id,
m.user_id,
m.mac_addr,
m.partner,
m.account_time,
if(n.record_date is null,'',n.record_date) as record_date,
m.activate_time,
if(n.log_time is null,'',n.log_time) as first_open,
'' as last_open,
m.status,
m.user_type,
m.now_app_vsersion,
m.update_history,
m.province,
m.city_name,
m.area_name
from test.demo1 m left join pb_clean.user_device n on m.mac_addr=n.dev_mac;
----------------------------
hadoop fs -get /test/user_info/000000_0 ./user_info/
mv 000000_0 'js_sp.csv'
拿回这个csv文件，添加一行：
main_account_id,child_account_id,user_id,mac_addr,partner,account_time,record_date,activate_time,first_open,last_open,status,user_type,now_app_vsersion,update_history,province,city_name,area_name
---------------
COPY user_info(main_account_id,child_account_id,user_id,mac_addr,partner,account_time,record_date,activate_time,first_open,last_open,status,user_type,now_app_version,update_history,province,city_name,area_name) FROM '~/Desktop/js_sp.csv' WITH HEADER = true ;
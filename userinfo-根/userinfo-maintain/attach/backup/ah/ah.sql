hive:
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
------------------------------
main_account_id     	string              	                    
child_account_id    	string              	                    
user_id             	string              	                    
mac_addr            	string              	                    
partner             	string              	                    
account_time        	string              	                    
recode_date         	string              	                    
activate_time       	string              	                    
first_open          	string              	                    
last_open           	string              	                    
status              	string              	                    
user_type           	string              	                    
now_app_vsersion    	string              	                    
update_history      	string              	                    
province            	string              	                    
city_name           	string              	                    
area_name           	string 
------------------------------
create table test.demo1 as
select if(u.area_no is null,'',u.area_no) as area_no,
if(m.main_account_id is null,'',m.main_account_id) as main_account_id,
if(m.child_account_id is null,'',m.child_account_id) as child_account_id,
if(m.child_account_id is null,'',m.child_account_id) as user_id,m.mac_addr,
'AH_CMCC' as partner,
"" as account_time,
if(m.status is null,'',m.status) as status,
if(m.user_type is null,'',m.user_type) as user_type 
from (select * from ah_cont_user_mac where mac_addr is not null) m left join ah_cont_user_child u on m.child_account_id=u.user_id;
-----------------------------------
create table test.demo2 as
select a.main_account_id,
a.child_account_id,
a.user_id,
a.mac_addr,
a.partner,
a.account_time,
'' as activate_time,
a.status,
a.user_type,
'' as now_app_vsersion,
'' as update_history,
'安徽省' as province,
if(b.city_name is null,'',b.city_name) as city_name,
if(b.area_name is null,'',b.area_name) as area_name from test.demo1 a left join ah_cont_user_area_info b on a.area_no=b.area_code;
-----------------------------------
insert into table test.user_info
select t1.main_account_id,
t1.child_account_id,
t1.user_id,
t1.mac_addr,
t1.partner,
t1.account_time,
if(t2.record_date is null,'',t2.record_date) as record_date,
t1.activate_time,
if(t2.log_time is null,'',t2.log_time) as first_open,
'' as last_open,
t1.status,
t1.user_type,
t1.now_app_vsersion,
t1.update_history,
t1.province,
t1.city_name,
t1.area_name
 from test.demo2 t1 left join pb_clean.user_device t2 on t1.mac_addr=t2.dev_mac;

hadoop fs -get /test/user_info/000000_0 ./user_info/

mv 000000_0 'ah.csv'
拿回这个csv文件，添加一行：
main_account_id,child_account_id,user_id,mac_addr,partner,account_time,record_date,activate_time,first_open,last_open,status,user_type,now_app_vsersion,update_history,province,city_name,area_name
----------------------------------------------------------------------
cassandra:
CREATE KEYSPACE likang
WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 3};
use likang;
----------------------------------------------------------------------
CREATE TABLE user_info (  
main_account_id text,  
child_account_id text,  
user_id text,
mac_addr text, 
partner text,
account_time timestamp,
record_date timestamp,
activate_time timestamp,
first_open timestamp,  
last_open timestamp,
status int,
user_type int,
now_app_version text,
update_history map<text,timestamp>,
province text,
city_name text,
area_name text,
PRIMARY KEY (partner, mac_addr)  
);

COPY user_info(main_account_id,child_account_id,user_id,mac_addr,partner,account_time,record_date,activate_time,first_open,last_open,status,user_type,now_app_version,update_history,province,city_name,area_name) FROM '~/Desktop/ah.csv' WITH HEADER = true ;
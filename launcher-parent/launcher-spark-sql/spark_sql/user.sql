--spark-sql-yarn.sh 5 8
--1
select P,count(distinct MC) from launcher_log_100 group by P;
--2
select P,MC,count(MC) from launcher_log_100 group by MC,P;
--3
select city_name,count(1) from (select a.*,b.city_name,b.user_id from launcher_log_100 a join userinfo.userinfo b on a.MC=b.mac_addr) c group by c.city_name;
--4 无数据
select * from launcher_log_101;
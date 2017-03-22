#!/bin/bash
today=`date -d '-1 day' +%Y-%m-%d`

spark-sql-yarn.sh 5 8 <<EOF
---it is for user
select P,count(distinct MC) from launcher_log_100 where yymmdd='2017-01-02' group by P;
select P,MC,count(MC) from launcher_log_100 where yymmdd='${today}' group by MC,P;
select city_name,count(1) from (select a.*,b.city_name,b.user_id from launcher_log_100 a join userinfo.userinfo b on a.MC=b.mac_addr) c group by c.city_name;
select P,V,count(1) from launcher_log_101 where yymmdd='${today}' group by V,P;

---it is for jump
select P,CTP,albumId,chnId,cpId,cpContentId,tvId,tagId,count(mc) from launcher_log_102 where yymmdd='${today}' group by P,CTP,albumId,chnId,cpId,cpContentId,tvId,tagId;
select P,x,y,count(1) from launcher_log_102 where yymmdd='${today}' group by P,x,y;
select P,SID,count(1),count(distinct mc) from launcher_log_102 where SID != 'NULL' and yymmdd='${today}' group by P,SID;
select P,count(1) from launcher_log_102 where x=-2 and yymmdd='${today}' group by P;

---it is for apk
select P,initType,initData,V from launcher_log_103 where yymmdd='${today}' group by P,initType,initData,V;
select P,re,count(1) from launcher_log_103 where yymmdd='${today}' group by re,P;
select P,count(1) from launcher_log_103 where yymmdd='${today}' where TP=2 group by P;
EOF
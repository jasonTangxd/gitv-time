--spark-sql-yarn.sh 5 8
select CTP,albumId,chnId,cpId,cpContentId,tvId,tagId,count(mc) from launcher_log_102 group by CTP,albumId,chnId,cpId,cpContentId,tvId,tagId;
select x,y,count(1) from launcher_log_102 group by x,y;
select SID,count(*),count(distinct mc) from launcher_log_102 where SID != 'NULL' group by SID;
select count(1) from launcher_log_102 where x=-2;
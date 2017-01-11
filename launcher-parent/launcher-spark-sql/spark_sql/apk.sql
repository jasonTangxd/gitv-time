--spark-sql-yarn.sh 5 8
select P,initType,initData,V from launcher_log_103 group by P,initType,initData,V;
select P,re,count(1) from launcher_log_103 group by re,P;
select P,count(1) from launcher_log_103 where TP=2 group by P;
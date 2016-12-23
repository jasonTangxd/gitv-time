--COPY userinfo.user_info (partner,mac_addr,account_time,activate_time,area_name,child_account_id,city_name,first_open,last_open,main_account_id,livod_app_version,vod_app_version,cnm_app_version,launcher_app_version,province,record_date,status,status_updatetime,livod_update_history,vod_update_history,cnm_update_history,launcher_update_history,user_id,user_type) TO 'userinfo.csv';
--hadoop fs -put userinfo.csv /userinfo/
CREATE TABLE userinfo (
    partner String,
    mac_addr String,
    account_time timestamp,
    activate_time timestamp,
    area_name String,
    child_account_id String,
    city_name String,
    first_open timestamp,
    last_open timestamp,
    main_account_id String,
    livod_app_version String,
    vod_app_version String,
    cnm_app_version String,
    launcher_app_version String,
    province String,
    record_date timestamp,
    status int,
    status_updatetime timestamp,
    livod_update_history STRING,
    vod_update_history STRING,
    cnm_update_history STRING,
    launcher_update_history STRING,
    user_id String,
    user_type int)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ','
STORED AS TEXTFILE
LOCATION '/userinfo';
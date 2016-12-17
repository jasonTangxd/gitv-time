## 项目介绍
通过pingback实时更新用户信息表,所有的update操作都是有条件进行的,distinguish的bolt会把流向分开，各取所需的执行
有的去rabbitMQ,有的用来更新第一次open时间，有的更新app_version

## 启动命令
```
/data/opt/soft/apache-storm-1.0.1/bin/storm jar uif.jar cn.gitv.bi.userinfo.uifmaintain.start.Start_up user_info
```

## 用户信息表创建
>
```
CREATE KEYSPACE userinfo WITH replication = {'class': 'NetworkTopologyStrategy', 'datacenter1': '3'}  AND durable_writes = true;
```
```
CREATE TABLE userinfo.frommysql_no_mac (
    mac text PRIMARY KEY,
    partner text
);
```
```
CREATE TABLE userinfo.user_info (
    partner text,
    mac_addr text,
    account_time timestamp,
    activate_time timestamp,
    area_name text,
    child_account_id text,
    city_name text,
    first_open timestamp,
    last_open timestamp,
    main_account_id text,
    livod_app_version text,
    vod_app_version text,
    cnm_app_version text,
    launcher_app_version text,
    province text,
    record_date timestamp,
    status int,
    status_updatetime timestamp,
    livod_update_history map<text, timestamp>,
    vod_update_history map<text, timestamp>,
    cnm_update_history map<text, timestamp>,
    launcher_update_history map<text, timestamp>,
    user_id text,
    user_type int,
    PRIMARY KEY (partner, mac_addr)
);
```
```
CREATE TABLE userinfo.nopartner_province (
    partner text PRIMARY KEY
);
```

## zk中维护的信息
``partnerToProvince``
```
create /uif_partner_about/partner_province/AAA AAAA测试
create /uif_partner_about/partner_province/AH_CMCC 安徽省
create /uif_partner_about/partner_province/AH_CMCC_TEST 安徽省
create /uif_partner_about/partner_province/BJ_CMCC 北京
create /uif_partner_about/partner_province/BYAHWW 安徽省
create /uif_partner_about/partner_province/BYHNTT 河南省
create /uif_partner_about/partner_province/BYHNYM 河南省
create /uif_partner_about/partner_province/FJLT 福建省
create /uif_partner_about/partner_province/GDJD 广东省
create /uif_partner_about/partner_province/GITV 银河院线
create /uif_partner_about/partner_province/HBJD 河北省
create /uif_partner_about/partner_province/HNYD 河南省
create /uif_partner_about/partner_province/JSLTJ 江苏省
create /uif_partner_about/partner_province/JSLTJD 江苏省
create /uif_partner_about/partner_province/JS_CMCC 江苏省
create /uif_partner_about/partner_province/JS_CMCC_CP 江苏省
create /uif_partner_about/partner_province/JS_CMCC_CP_UAT 江苏省
create /uif_partner_about/partner_province/JS_CUCC 江苏省
create /uif_partner_about/partner_province/JXNCNW 江西省
create /uif_partner_about/partner_province/KMYD 云南省
create /uif_partner_about/partner_province/KMYDZX 云南省
create /uif_partner_about/partner_province/NMGD 内蒙古
create /uif_partner_about/partner_province/SAXYD 陕西省
create /uif_partner_about/partner_province/SCIPTV 四川省
create /uif_partner_about/partner_province/SDCMNET 山东省
create /uif_partner_about/partner_province/SDTV 山东省
create /uif_partner_about/partner_province/SD_CMCB_JN 山东省
create /uif_partner_about/partner_province/SD_CMCC_JN 山东省
create /uif_partner_about/partner_province/SD_CMCC_QD 山东省
create /uif_partner_about/partner_province/SHJD 上海
create /uif_partner_about/partner_province/TJCNC 天津
create /uif_partner_about/partner_province/WHBY 湖北省
create /uif_partner_about/partner_province/YNYDHW 云南省
create /uif_partner_about/partner_province/YNYDZX 云南省
create /uif_partner_about/partner_province/YSLW 老挝
create /uif_partner_about/partner_province/ZJLTJD 浙江省
create /uif_partner_about/partner_province/ZJQD 浙江省
create /uif_partner_about/partner_province/ZJYD 浙江省
create /uif_partner_about/partner_province/ZJYDJD 浙江省
create /uif_partner_about/partner_province/ZJ_CMCC 浙江省
```

### 简单小结
**about properties**
>src/main/ resources|java 类似properties会自动打包到jar中,直接"/xx.properties"

**about zk ls**
>ls /uif_partner_about/partner_province 所有的合作伙伴对应的省份

>ls /uif_partner_about/builded_consumer 已经写好consumer的合作伙伴

**about zk create**
>create /uif_partner_about/builded_consumer/AH_CMCC true

>create /uif_partner_about/builded_consumer/JS_CMCC true

>create /uif_partner_about/builded_consumer/JS_CMCC_CP true

**``业务上总结``**

>需要先区分：LIV、VOD、CNM

>再区分：1.0、2.0

>所以代码中都通过top_filter这个bolt做完过滤
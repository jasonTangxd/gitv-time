## USER_INFO
具体的README.md在各个子项目中介绍

``userinfo-app-version``

``userinfo-consumer-mysql``

``userinfo-maintain``

``userinfo-script-change``


## Detail
#### 1.uif_maintain
* cassandra中维护一张完整的用户信息表，动态的在更新(user_info)
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

* storm中的处理逻辑

收集log-clean-10的升级信息

![](http://p1.bpimg.com/567571/17169ba1329b2bee.png)

#### 2.uif_consumer_mysql
* 103机器上的rabbitMQ的consumer

``从数量(1000个mac地址) 和时间（10min）两个维度去项目组的mysql同步数据``
```
->启动命令：nohup /data/soft/rabbitmq_server-3.6.5/sbin/rabbitmq-server > /data/soft/rabbitmq_server-3.6.5/sbin/nohup.out 2>&1 &
->启动后台java程序：nohup java -jar uif_consumer.jar > nohup.out 2>&1 &
->该外挂用到了c3po的线程池，配置（c3p0-config.xml）
```

* zj同步hive全量数据copy :csv到cassandra的user_info  [10.10.121.57]

* mysql数据同步全量表过来成csv，然后通过python脚本逐行循环to Cassandra  [10.10.121.58]

#### 3.uif_app_version
收集log-clean-10的升级信息
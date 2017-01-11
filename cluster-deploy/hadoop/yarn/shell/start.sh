#!/bin/bash
#分别启动m1、m2的rm
ssh 10.10.121.139 "/data/opt/soft/hadoop-2.7.3/sbin/yarn-daemon.sh cn.bi.gitv.hip.parquetdemo.start resourcemanager"
ssh 10.10.121.138 "/data/opt/soft/hadoop-2.7.3/sbin/yarn-daemon.sh cn.bi.gitv.hip.parquetdemo.start resourcemanager"
#在m1上启动所有的slaves中的nm
ssh 10.10.121.139 "/data/opt/soft/hadoop-2.7.3/sbin/yarn-daemons.sh cn.bi.gitv.hip.parquetdemo.start nodemanager"
##启动m1 m2的proxy
#ssh 10.10.121.138 "/data/opt/soft/hadoop-2.7.3/sbin/yarn-daemon.sh cn.bi.gitv.hip.parquetdemo.start proxyserver"
#ssh 10.10.121.139 "/data/opt/soft/hadoop-2.7.3/sbin/yarn-daemon.sh cn.bi.gitv.hip.parquetdemo.start proxyserver"
#在master2上启动jobhistory
ssh 10.10.121.138 "/data/opt/soft/hadoop-2.7.3/sbin/mr-jobhistory-daemon.sh cn.bi.gitv.hip.parquetdemo.start historyserver"







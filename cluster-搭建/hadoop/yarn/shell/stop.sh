#!/bin/bash
#先关闭所有的nm
ssh 10.10.121.139 "/data/opt/soft/hadoop-2.7.3/sbin/yarn-daemons.sh stop nodemanager"
#分别关闭m1、m2上的rm
ssh 10.10.121.139 "/data/opt/soft/hadoop-2.7.3/sbin/yarn-daemon.sh stop resourcemanager"
ssh 10.10.121.138 "/data/opt/soft/hadoop-2.7.3/sbin/yarn-daemon.sh stop resourcemanager"


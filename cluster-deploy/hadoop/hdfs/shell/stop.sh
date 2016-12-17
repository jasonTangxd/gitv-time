#!/bin/bash
#在master1上stop所有slaves的dn
ssh 10.10.121.139 "/data/opt/soft/hadoop-2.7.3/sbin/hadoop-daemons.sh stop datanode"

ssh 10.10.121.139 "/data/opt/soft/hadoop-2.7.3/sbin/hadoop-daemon.sh stop namenode"
ssh 10.10.121.138 "/data/opt/soft/hadoop-2.7.3/sbin/hadoop-daemon.sh stop namenode"

#关闭jn
ssh 10.10.121.148 "/data/opt/soft/hadoop-2.7.3/sbin/hadoop-daemon.sh stop journalnode"
ssh 10.10.121.149 "/data/opt/soft/hadoop-2.7.3/sbin/hadoop-daemon.sh stop journalnode"
ssh 10.10.121.150 "/data/opt/soft/hadoop-2.7.3/sbin/hadoop-daemon.sh stop journalnode"
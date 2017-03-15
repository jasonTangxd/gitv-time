#!/bin/bash
#首先启动jn,共享editLog
ssh 10.10.121.148 "/data/opt/soft/hadoop-2.7.3/sbin/hadoop-daemon.sh start journalnode"
ssh 10.10.121.149 "/data/opt/soft/hadoop-2.7.3/sbin/hadoop-daemon.sh start journalnode"
ssh 10.10.121.150 "/data/opt/soft/hadoop-2.7.3/sbin/hadoop-daemon.sh start journalnode"
#在m1、m2上启动zkfc
ssh 10.10.121.139 "/data/opt/soft/hadoop-2.7.3/sbin/hadoop-daemon.sh start zkfc"
ssh 10.10.121.138 "/data/opt/soft/hadoop-2.7.3/sbin/hadoop-daemon.sh start zkfc"
#启动nn
ssh 10.10.121.139 "/data/opt/soft/hadoop-2.7.3/sbin/hadoop-daemon.sh start namenode"
ssh 10.10.121.138 "/data/opt/soft/hadoop-2.7.3/sbin/hadoop-daemon.sh start namenode"
#启动所有slaves的dn（slaves中包含m1,m2）
ssh 10.10.121.139 "/data/opt/soft/hadoop-2.7.3/sbin/hadoop-daemons.sh start datanode"
#/data/opt/soft/hadoop-2.7.3/sbin/hadoop-daemon.sh cn.bi.gitv.hip.parquetdemo.start datanode
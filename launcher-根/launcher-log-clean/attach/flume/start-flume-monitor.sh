#!/bin/bash
flume=$1
conf=$2
monitorApp=$3
cd ${FLUME_HOME}
nohup ./bin/flume-ng agent  -c ./conf -f ./conf/${conf} -n ${flume} -Dflume.monitoring.type=cn.gitv.bi.flume.monitor.ZkMonitorService -Dflume.monitoring.zk.address=10.10.121.120:2281,10.10.121.121:2281,10.10.121.138:2281,10.10.121.148:2281,10.10.121.150:2281  -Dflume.monitoring.app=${monitorApp} >>./${monitorApp}.log 2>&1 &
#!/bin/bash
#测试配置文件
nutcracker -t
nutcracker -d -c /data/opt/soft/twemproxy-1.3.20/conf/nutcracker.yml -p /data/opt/soft/twemproxy-1.3.20/run/redisproxy.pid -o /data/opt/soft/twemproxy-1.3.20/run/redisproxy.log
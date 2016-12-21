## 执行hadoop jar
```
hadoop jar kafka-hadoop-loader.jar -c off -g khloader -o earliest -t launcher-log-100 -z 'slave7.gitv.rack2.bk:2281,slave8.gitv.rack2.bk:2281,slave9.gitv.rack2.bk:2281,slave10.gitv.rack2.bk:2281,slave11.gitv.rack2.bk:2281,master2.gitv.rack2.bk:2281,master1.gitv.rack1.bk:2281,slave1.gitv.rack1.bk:2281,slave2.gitv.rack1.bk:2281,slave3.gitv.rack1.bk:2281,slave4.gitv.rack1.bk:2281' /tmp
```
## 查看数据
```
hadoop fs -ls /tmp/launcher-log-100/
```
## 删除Consumer
```
zkCli.sh -server localhost:2281

>rmr /consumers/khloader
```
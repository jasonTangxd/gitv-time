## 作为客户端配置
* ``终然只是一个客户端,为了history可点击，需要配置``
```
[spark-default.conf] ：spark.eventLog.dir [hdfs://ns/tmp/spark-events]

[spark-env.sh] ：spark.history.fs.logDirectory [hdfs://ns/tmp/spark-events]
```
``两者分别在两个配置文件中配置，但是指向路径需要一样``

* 然后通过这个配置的机器上启动history-server:
```
/data/opt/soft/spark-2.0.1-bin-hadoop2.7/sbin/start-history-server.sh
```
## 测试
**测试yarn上的spark日志跳转demo**
```
cd /data/opt/soft/spark-2.0.1-bin-hadoop2.7
```

```
bin/spark-submit --master yarn --deploy-mode cluster --driver-memory 1g --executor-memory 1g --executor-cores 2 examples/src/main/python/pi.py 1000
```
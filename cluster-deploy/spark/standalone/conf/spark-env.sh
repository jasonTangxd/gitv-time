#!/usr/bin/env bash
export SPARK_HOME=/data/opt/soft/spark-2.0.1-bin-hadoop2.7
export HADOOP_HOME=/data/opt/soft/hadoop-2.7.3
export SCALA_HOME=/data/opt/soft/scala-2.11.0
export JAVA_HOME=/data/opt/soft/jdk1.8.0_112
export YARN_CONF_DIR=$HADOOP_HOME/etc/hadoop
export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop
export SPARK_LIBARY_PATH=.:$JAVA_HOME/lib:$JAVA_HOME/jre/lib:$HADOOP_HOME/lib/native
export SPARK_DAEMON_JAVA_OPTS="-Dspark.deploy.recoveryMode=ZOOKEEPER -Dspark.deploy.zookeeper.url=slave7.gitv.rack2.bk:2281,slave8.gitv.rack2.bk:2281,slave9.gitv.rack2.bk:2281,slave10.gitv.rack2.bk:2281,slave11.gitv.rack2.bk:2281,master2.gitv.rack2.bk:2281,master1.gitv.rack1.bk:2281,slave1.gitv.rack1.bk:2281,slave2.gitv.rack1.bk:2281,slave3.gitv.rack1.bk:2281,slave4.gitv.rack1.bk:2281 -Dspark.deploy.zookeeper.dir=/spark"
#SPARK_MASTER_IP=master1.gitv.rack1.bk
SPARK_WORKER_MEMORY=120G
SPARK_WORKER_CORES=24
SPARK_WORKER_DIR=/data1/info/spark/worker
SPARK_LOG_DIR=/data1/info/spark/logs

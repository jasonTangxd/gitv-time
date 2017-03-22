#!/usr/bin/env bash
export SPARK_HOME=/data/opt/soft/spark-2.0.1-bin-hadoop2.7
export HADOOP_HOME=/data/opt/soft/hadoop-2.7.3
export SCALA_HOME=/data/opt/soft/scala-2.11.0
export JAVA_HOME=/data/opt/soft/jdk1.8.0_112
export YARN_CONF_DIR=$HADOOP_HOME/etc/hadoop
export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop
export SPARK_LIBARY_PATH=.:$JAVA_HOME/lib:$JAVA_HOME/jre/lib:$HADOOP_HOME/lib/native
SPARK_WORKER_DIR=/data1/info/spark/worker
SPARK_LOG_DIR=/data1/info/spark/logs
export SPARK_HISTORY_OPTS="-Dspark.history.ui.port=18080 -Dspark.history.retainedApplications=3 -Dspark.history.fs.logDirectory=hdfs://ns/tmp/spark-events"

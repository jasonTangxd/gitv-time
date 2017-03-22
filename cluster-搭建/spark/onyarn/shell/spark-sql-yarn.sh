#!/bin/bash
#因为需要和终端交互 故现在提供的只有client模式，spark driver在当前机器上
spark-sql --master yarn --deploy-mode client --executor-memory $1G --num-executors $2
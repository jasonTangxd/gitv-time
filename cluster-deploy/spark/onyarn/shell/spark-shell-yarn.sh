#!/bin/bash
spark-shell --master yarn --deploy-mode client --executor-memory $1G --num-executors $2
#!/bin/bash
#$2=cluster or client
spark-submit --class $1 --master yarn --deploy-mode $2 --driver-memory $3g --executor-memory $4g --executor-cores $5 $6
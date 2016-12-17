#!/bin/bash
mkdir -p /data1/info/redis/log_55555/
mkdir -p /data1/info/redis/log_55556/
mkdir -p /data1/info/redis/log_55557/
mkdir -p /data1/info/redis/log_55558/

/data/opt/soft/redis-3.2.1/src/redis-server /data/opt/soft/redis-3.2.1/conf/redis_vod1.conf

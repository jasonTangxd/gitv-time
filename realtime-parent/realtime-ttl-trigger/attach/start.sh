#!/bin/bash
#148
touch /tmp/ttl_gc_log/ttl-trigger-vod1-gc.log
touch /tmp/ttl_gc_log/ttl-trigger-vod2-gc.log
touch /tmp/ttl_gc_log/ttl-trigger-vod3-gc.log
touch /tmp/ttl_gc_log/ttl-trigger-vod4-gc.log
touch /tmp/ttl_gc_log/ttl-trigger-vod5-gc.log
touch /tmp/ttl_gc_log/ttl-trigger-vod6-gc.log
touch /tmp/ttl_gc_log/ttl-trigger-vod7-gc.log
touch /tmp/ttl_gc_log/ttl-trigger-vod8-gc.log
touch /tmp/ttl_gc_log/ttl-trigger-vod9-gc.log
touch /tmp/ttl_gc_log/ttl-trigger-vod10-gc.log
touch /tmp/ttl_gc_log/ttl-trigger-vod11-gc.log
touch /tmp/ttl_gc_log/ttl-trigger-vod12-gc.log
touch /tmp/ttl_gc_log/ttl-trigger-vod13-gc.log
touch /tmp/ttl_gc_log/ttl-trigger-vod14-gc.log
touch /tmp/ttl_gc_log/ttl-trigger-vod15-gc.log
touch /tmp/ttl_gc_log/ttl-trigger-vod16-gc.log

nohup java -Xms2G -Xmx5G -Xloggc:/tmp/ttl_gc_log/ttl-trigger-vod1-gc.log -jar ttl-trigger.jar -p 55555 -i 10.10.121.151 -t vod  >> nohup.out 2>&1 &
nohup java -Xms2G -Xmx5G -Xloggc:/tmp/ttl_gc_log/ttl-trigger-vod2-gc.log -jar ttl-trigger.jar -p 55556 -i 10.10.121.151 -t vod  >> nohup.out 2>&1 &

nohup java -Xms2G -Xmx5G -Xloggc:/tmp/ttl_gc_log/ttl-trigger-vod3-gc.log -jar ttl-trigger.jar -p 55555 -i 10.10.121.152 -t vod  >> nohup.out 2>&1 &
nohup java -Xms2G -Xmx5G -Xloggc:/tmp/ttl_gc_log/ttl-trigger-vod4-gc.log -jar ttl-trigger.jar -p 55556 -i 10.10.121.152 -t vod  >> nohup.out 2>&1 &

nohup java -Xms2G -Xmx5G -Xloggc:/tmp/ttl_gc_log/ttl-trigger-vod5-gc.log -jar ttl-trigger.jar -p 55555 -i 10.10.121.153 -t vod  >> nohup.out 2>&1 &
nohup java -Xms2G -Xmx5G -Xloggc:/tmp/ttl_gc_log/ttl-trigger-vod6-gc.log -jar ttl-trigger.jar -p 55556 -i 10.10.121.153 -t vod  >> nohup.out 2>&1 &

nohup java -Xms2G -Xmx5G -Xloggc:/tmp/ttl_gc_log/ttl-trigger-vod7-gc.log -jar ttl-trigger.jar -p 55555 -i 10.10.121.138 -t vod  >> nohup.out 2>&1 &
nohup java -Xms2G -Xmx5G -Xloggc:/tmp/ttl_gc_log/ttl-trigger-vod8-gc.log -jar ttl-trigger.jar -p 55556 -i 10.10.121.138 -t vod  >> nohup.out 2>&1 &

nohup java -Xms2G -Xmx5G -Xloggc:/tmp/ttl_gc_log/ttl-trigger-vod9-gc.log -jar ttl-trigger.jar -p 55555 -i 10.10.121.139 -t vod  >> nohup.out 2>&1 &
nohup java -Xms2G -Xmx5G -Xloggc:/tmp/ttl_gc_log/ttl-trigger-vod10-gc.log -jar ttl-trigger.jar -p 55556 -i 10.10.121.139 -t vod >> nohup.out 2>&1 &

nohup java -Xms2G -Xmx5G -Xloggc:/tmp/ttl_gc_log/ttl-trigger-vod11-gc.log -jar ttl-trigger.jar -p 55555 -i 10.10.121.148 -t vod >> nohup.out 2>&1 &
nohup java -Xms2G -Xmx5G -Xloggc:/tmp/ttl_gc_log/ttl-trigger-vod12-gc.log -jar ttl-trigger.jar -p 55556 -i 10.10.121.148 -t vod >> nohup.out 2>&1 &

nohup java -Xms2G -Xmx5G -Xloggc:/tmp/ttl_gc_log/ttl-trigger-vod13-gc.log -jar ttl-trigger.jar -p 55555 -i 10.10.121.149 -t vod >> nohup.out 2>&1 &
nohup java -Xms2G -Xmx5G -Xloggc:/tmp/ttl_gc_log/ttl-trigger-vod14-gc.log -jar ttl-trigger.jar -p 55556 -i 10.10.121.149 -t vod >> nohup.out 2>&1 &

nohup java -Xms2G -Xmx5G -Xloggc:/tmp/ttl_gc_log/ttl-trigger-vo15-gc.log -jar ttl-trigger.jar -p 55555 -i 10.10.121.150 -t vod  >> nohup.out 2>&1 &
nohup java -Xms2G -Xmx5G -Xloggc:/tmp/ttl_gc_log/ttl-trigger-vod16-gc.log -jar ttl-trigger.jar -p 55556 -i 10.10.121.150 -t vod >> nohup.out 2>&1 &

#to 151
touch /tmp/ttl_gc_log/ttl-trigger-liv1-gc.log
touch /tmp/ttl_gc_log/ttl-trigger-liv2-gc.log
touch /tmp/ttl_gc_log/ttl-trigger-liv3-gc.log
touch /tmp/ttl_gc_log/ttl-trigger-liv4-gc.log

nohup java -Xms2G -Xmx5G -Xloggc:/tmp/ttl_gc_log/ttl-trigger-liv1-gc.log -jar ttl-trigger.jar -p 55557 -i 10.10.121.151 -t liv >> nohup.out 2>&1 &
nohup java -Xms2G -Xmx5G -Xloggc:/tmp/ttl_gc_log/ttl-trigger-liv2-gc.log -jar ttl-trigger.jar -p 55558 -i 10.10.121.151 -t liv >> nohup.out 2>&1 &
nohup java -Xms2G -Xmx5G -Xloggc:/tmp/ttl_gc_log/ttl-trigger-liv3-gc.log -jar ttl-trigger.jar -p 55557 -i 10.10.121.152 -t liv >> nohup.out 2>&1 &
nohup java -Xms2G -Xmx5G -Xloggc:/tmp/ttl_gc_log/ttl-trigger-liv4-gc.log -jar ttl-trigger.jar -p 55558 -i 10.10.121.152 -t liv >> nohup.out 2>&1 &

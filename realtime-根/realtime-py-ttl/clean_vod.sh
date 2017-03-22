#!/bin/bash
#comp
/data/opt/soft/redis-3.2.1/src/redis-cli -h 10.10.121.120 -a 1534101571 -p 55555 <<EOF
FLUSHDB
FLUSHALL
exit
EOF

#vod 55555
/data/opt/soft/redis-3.2.1/src/redis-cli -h 10.10.121.151 -p 55555 <<EOF
FLUSHDB
FLUSHALL
exit
EOF
/data/opt/soft/redis-3.2.1/src/redis-cli -h 10.10.121.152 -p 55555 <<EOF
FLUSHDB
FLUSHALL
exit
EOF
/data/opt/soft/redis-3.2.1/src/redis-cli -h 10.10.121.153 -p 55555 <<EOF
FLUSHDB
FLUSHALL
exit
EOF
/data/opt/soft/redis-3.2.1/src/redis-cli -h 10.10.121.138 -p 55555 <<EOF
FLUSHDB
FLUSHALL
exit
EOF
/data/opt/soft/redis-3.2.1/src/redis-cli -h 10.10.121.139 -p 55555 <<EOF
FLUSHDB
FLUSHALL
exit
EOF
/data/opt/soft/redis-3.2.1/src/redis-cli -h 10.10.121.148 -p 55555 <<EOF
FLUSHDB
FLUSHALL
exit
EOF
/data/opt/soft/redis-3.2.1/src/redis-cli -h 10.10.121.149 -p 55555 <<EOF
FLUSHDB
FLUSHALL
exit
EOF
/data/opt/soft/redis-3.2.1/src/redis-cli -h 10.10.121.150 -p 55555 <<EOF
FLUSHDB
FLUSHALL
exit
EOF
#vod 55556
/data/opt/soft/redis-3.2.1/src/redis-cli -h 10.10.121.151 -p 55556 <<EOF
FLUSHDB
FLUSHALL
exit
EOF
/data/opt/soft/redis-3.2.1/src/redis-cli -h 10.10.121.152 -p 55556 <<EOF
FLUSHDB
FLUSHALL
exit
EOF
/data/opt/soft/redis-3.2.1/src/redis-cli -h 10.10.121.153 -p 55556 <<EOF
FLUSHDB
FLUSHALL
exit
EOF
/data/opt/soft/redis-3.2.1/src/redis-cli -h 10.10.121.138 -p 55556 <<EOF
FLUSHDB
FLUSHALL
exit
EOF
#
/data/opt/soft/redis-3.2.1/src/redis-cli -h 10.10.121.139 -p 55556 <<EOF
FLUSHDB
FLUSHALL
exit
EOF
/data/opt/soft/redis-3.2.1/src/redis-cli -h 10.10.121.148 -p 55556 <<EOF
FLUSHDB
FLUSHALL
exit
EOF
/data/opt/soft/redis-3.2.1/src/redis-cli -h 10.10.121.149 -p 55556 <<EOF
FLUSHDB
FLUSHALL
exit
EOF
/data/opt/soft/redis-3.2.1/src/redis-cli -h 10.10.121.150 -p 55556 <<EOF
FLUSHDB
FLUSHALL
exit
EOF

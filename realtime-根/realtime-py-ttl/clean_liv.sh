#!/bin/bash
#comp
/data/opt/soft/redis-3.2.1/src/redis-cli -h 10.10.121.120 -a 1534101571 -p 55556 <<EOF
FLUSHDB
FLUSHALL
exit
EOF
/data/opt/soft/redis-3.2.1/src/redis-cli -h 10.10.121.120 -a 1534101571 -p 55557 <<EOF
FLUSHDB
FLUSHALL
exit
EOF

#liv
/data/opt/soft/redis-3.2.1/src/redis-cli -h 10.10.121.151 -p 55557 <<EOF
FLUSHDB
FLUSHALL
exit
EOF
/data/opt/soft/redis-3.2.1/src/redis-cli -h 10.10.121.151 -p 55558 <<EOF
FLUSHDB
FLUSHALL
exit
EOF
/data/opt/soft/redis-3.2.1/src/redis-cli -h 10.10.121.152 -p 55557 <<EOF
FLUSHDB
FLUSHALL
exit
EOF
/data/opt/soft/redis-3.2.1/src/redis-cli -h 10.10.121.152 -p 55558 <<EOF
FLUSHDB
FLUSHALL
exit
EOF
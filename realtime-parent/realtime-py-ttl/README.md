## 项目介绍
对于ttl到期触发的监听处理逻辑

## 脚本介绍
* ``clean_liv.sh``
* ``clean_vod.sh``
分别用来清空redis直播、点播缓存的所有数据(mac full + count num)

* ``cn.bi.gitv.hip.parquetdemo.start.sh``
在一台机器上启动多个ttl进程，每个进程监听hash分的一台redis机器上的ttl触发消息

* ``ttl.py.bk``
一个处理收到TTL消息的py通用脚本

* ``kill all py``
因为启动16+4的监听ttl脚本,一个个kill很麻烦，故：
```
    ps -emf|grep ttl|awk -F ' ' '{print $2}'|xargs kill
```


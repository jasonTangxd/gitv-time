## 机器规划
```
10.10.121.119   slave7.gitv.rack2.bk
10.10.121.120   slave8.gitv.rack2.bk
10.10.121.121   slave9.gitv.rack2.bk
10.10.121.122   slave10.gitv.rack2.bk
10.10.121.123   slave11.gitv.rack2.bk
10.10.121.138	master2.gitv.rack2.bk
(rm:yarn.resourcemanager.hostname)
```
```
10.10.121.139   master1.gitv.rack1.bk
(rm:yarn.resourcemanager.hostname)
10.10.121.148   slave1.gitv.rack1.bk
(jn)_/data/opt/soft/hadoop-2.7.3/sbin/hadoop-daemon.sh start journalnode
10.10.121.149   slave2.gitv.rack1.bk
(jn)/data/opt/soft/hadoop-2.7.3/sbin/hadoop-daemon.sh start journalnode
10.10.121.150   slave3.gitv.rack1.bk
(jn)/data/opt/soft/hadoop-2.7.3/sbin/hadoop-daemon.sh start journalnode
10.10.121.151   slave4.gitv.rack1.bk
10.10.121.152   slave5.gitv.rack1.bk
10.10.121.153   slave6.gitv.rack1.bk
```
## 免密钥
* 所有机器ssh-keygen -t rsa生成密钥

* 拿到各机器的pub文件
```
scp 10.10.121.153:~/.ssh/id_rsa.pub ./	
```

* 统一追加到40上的一个文件中
```
cat id_rsa.pub >> authorized_keys
```

* 再将这个文件分到各个机器的~/.ssh/下

>jn在138、139、148

>rm在149

## 机架感知实现
``参见core-site.xml``
```
<property>
  <name>topology.script.file.name</name>
  <value>/data/opt/soft/hadoop-2.7.3/etc/hadoop/rack_aware.py</value>
</property>
```
```
rack = {"master2.gitv.rack2.bk":"rack2",
        "slave7.gitv.rack2.bk":"rack2",
        "slave8.gitv.rack2.bk":"rack2",
        "slave9.gitv.rack2.bk":"rack2",
        "slave10.gitv.rack2.bk":"rack2",
        "slave11.gitv.rack2.bk":"rack2",
        "slave1.gitv.rack1.bk":"rack1",
        "slave2.gitv.rack1.bk":"rack1",
        "slave3.gitv.rack1.bk":"rack1",
        "slave4.gitv.rack1.bk":"rack1",
        "slave5.gitv.rack1.bk":"rack1",
        "slave6.gitv.rack1.bk":"rack1",
        "master1.gitv.rack1.bk":"rack1",
        "10.10.121.138":"rack2",
        "10.10.121.119":"rack2",
        "10.10.121.120":"rack2",
        "10.10.121.121":"rack2",
        "10.10.121.122":"rack2",
        "10.10.121.123":"rack2",
        "10.10.121.148":"rack1",
        "10.10.121.149":"rack1",
        "10.10.121.150":"rack1",
        "10.10.121.151":"rack1",
        "10.10.121.152":"rack1",
        "10.10.121.153":"rack1",
        "10.10.121.139":"rack1",
        }
```
``后证明 该py脚本需要放到hadoop/bin下+赋予执行权限``

查看机架情况
```
hadoop dfsadmin -printTopology 
```


``最后实现nn的ha   yarn的ha   机架感知``
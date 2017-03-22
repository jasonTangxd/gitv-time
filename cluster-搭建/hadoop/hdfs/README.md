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

## 集群的启动
#### 1.1 启动zookeeper集群
分别在mast1、mast2、mast3上执行如下命令启动zookeeper集群；
```
[hadoop@Mast1 bin]$ sh zkServer.sh start
```
验证集群zookeeper集群是否启动，分别在mast1、mast2、mast3上执行如下命令验证zookeeper集群是否启动，集群启动成功，有两个follower节点跟一个leader节点；
```
[hadoop@Mast1 bin]$ sh zkServer.sh status
```
JMX enabled by default
Using config: /home/hadoop/zookeeper/zookeeper-3.3.6/bin/../conf/zoo.cfg
Mode: follower

#### 1.2 启动journalnode集群
在master1上执行如下命令完成JournalNode集群的启动
```
[hadoop@Mast1 hadoop-2.5.2]$ sbin/hadoop-daemon.sh start journalnode
```
执行jps命令，可以查看到JournalNode的java进程pid

#### 1.3 格式化zkfc,让在zookeeper中生成ha节点
在master1上执行如下命令，完成格式化
```
hdfs zkfc -formatZK
```

（注意，这条命令最好手动输入，直接copy执行有可能会有问题，当时部署时我是蛋疼了许久）
格式成功后，查看zookeeper中可以看到
```
[zk: localhost:2181(CONNECTED) 1] ls /hadoop-ha
[ns]
```

#### 1.4 格式化hdfs
```
hadoop namenode -format
```
``（注意，这条命令最好手动输入，直接copy执行有可能会有问题）``

#### 1.5 启动NameNode
首先在master1上启动active节点，在masterer1上执行如下命令
```
[hadoop@master1 hadoop-2.5.2]$ sbin/hadoop-daemon.sh start namenode
```

在master2上同步namenode的数据，同时启动standby的namenod,命令如下

#把NameNode的数据同步到master2上
```
[hadoop@master2 hadoop-2.5.2]$ hdfs namenode -bootstrapStandby
```

#启动mast2上的namenode作为standby
```
[hadoop@master2 hadoop-2.5.2]$ sbin/hadoop-daemon.sh start namenode
```

#### 1.6 启动datanode
在master1上执行如下命令
```
[hadoop@Mast1 hadoop-2.5.2]$ sbin/hadoop-daemons.sh start datanode
```

#### 1.7 启动yarn
> 在作为资源管理器上的机器上启动，我这里是master1,执行如下命令完成yarn的启动
```
[hadoop@Mast3 hadoop-2.5.2]$ sbin/start-yarn.sh (会启动ResourceManager和各个子节点的NodeManager)
```

#### 1.8 启动ZKFC
分别在master1、master2上执行如下命令，完成ZKFC的启动
```
[hadoop@master1 hadoop-2.5.2]$ sbin/hadoop-daemon.sh start zkfc
[hadoop@master2 hadoop-2.5.2]$ sbin/hadoop-daemon.sh start zkfc
```
启动了守护进程：DFSZKFailoverController


``最后实现nn的ha   yarn的ha   机架感知``
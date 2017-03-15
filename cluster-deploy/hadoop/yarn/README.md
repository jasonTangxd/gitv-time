## 基础知识梳理
``图例``

![](http://p1.bpimg.com/567571/596d1a9786e0b92f.png)

步骤1:
```
用户向YARN中提交应用程序，其中包括ApplicationMaster程序、启动ApplicationMaster的命令、用户程序等
```
步骤2:
```
ResourceManager为该应用程序分配第一个Container，并与对应的若干个Node-Manager通信，要求它在这个Container中启动应用程序的ApplicationMaster。
```
步骤3:
每台node都有一个唯一的ApplicationMaster
```
ApplicationMaster首先向ResourceManager注册，这样用户可以直接通过ResourceManager查看应用程序的运行状态，然后它将为各个任务申请资源，并监控它的运行状态，直到运行结束，即重复步骤4~7
```
步骤4:
```
ApplicationMaster采用轮询的方式通过RPC协议向ResourceManager申请和领取资源
```
步骤5:
```
一旦ApplicationMaster申请到资源后，便与对应的NodeManager通信，要求它启动任务
```
步骤6:
```
NodeManager为任务设置好运行环境（包括环境变量、JAR包、二进制程序等）后，将任务启动命令写到一个脚本中，并通过运行该脚本启动任务
```
步骤7:
```
各个任务通过某个RPC协议向ApplicationMaster汇报自己的状态和进度，以让ApplicationMaster随时掌握各个任务的运行状态，从而可以在任务失败时重新启动任务。
在应用程序运行过程中，用户可随时通过RPC向ApplicationMaster查询应用程序的当前运行状态
```
步骤8:
```
应用程序运行完成后，ApplicationMaster向ResourceManager注销并关闭自己
```

--------
``图例``

![](http://p1.bpimg.com/567571/c872173144930d3e.jpg)
```
可将YARN看做一个云操作系统，它负责为应用程序启动ApplicationMaster（相当于主线程），然后再由ApplicationMaster负责数据切分、任务分配、启动和监控等工作，
而由ApplicationMaster启动的各个Task（相当于子线程）仅负责自己的计算任务。当所有任务计算完成后，ApplicationMaster认为应用程序运行完成，然后退出
```

## YARN集群搭建
建立在hdfs基础上,节点减少为8个新机器，主要参考配置
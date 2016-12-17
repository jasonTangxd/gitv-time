# SPARK部署分为三种模式
## YARN模式：
采用yarn模式的话，其实就是把spark作为一个客户端提交作业给YARN，实际运行程序的是YARN，就不需要部署多个节点，部署一个节点就可以了。

## Standalone模式:
自带的调度方式,启动master和slave（work）节点

## Mesos模式

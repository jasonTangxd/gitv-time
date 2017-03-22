## 项目简介
这个topo用来对升级的用户做信息记载,记载历史版本号以及当前版本号更新
## 简单记录
topo启动命令记录
```
/data/opt/soft/apache-storm-1.0.1/bin/storm jar uif_app.jar cn.gitv.bi.userinfo.appupdate.start.Start_up uif_app
```
cql简单查看三个类型的版本情况
```
select livod_update_history,vod_update_history,cnm_update_history from user_info where partner='JS_CMCC_CP';
```
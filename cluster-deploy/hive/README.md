## 下载地址
http://mirrors.hust.edu.cn/apache/hive/hive-1.2.1/

## MYSQL相关
* mysql更改root用户的密码
```
mysql> UPDATE user SET Password=PASSWORD('123456') where USER='root';
```
```
mysql> FLUSH PRIVILEGES;
```
* 给库hive_meta授权
```
GRANT ALL ON *.* TO 'hive_meta'@'%';
```
* 授权用户root使用密码123456从任意主机连接到mysql服务器
```
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY '123456' WITH GRANT OPTION;
```

## HIVE相关
* 修改hive-site配置文件
* 复制MySQL的JDBC驱动包到Hive的lib目录下
* 初始化meta
```
${hive_home}/bin/schematool -dbType mysql -initSchema
```

* Starting Hive Metastore Server
> 若配置了 hive.metastore.uris ，则必须首先启动 ThriftMetastore服务端 ，然后才能启动 hive 客户端；否则，可直接启动 hive 客户端

>metastore的作用是你可以通过thrift的方式访问hive的元数据库、比如你在a机器上面启动了一个metastore服务、然后在b机器上你装hive的时候，你就可以不用写hive元数据的真实地址了

>(可以将hive拆分成metastore的服务端+hive客户端两部分（c、s）)

```
nohup hive --service metastore >nohup.out 2>&1 &
```
* 启动hiveserver
>在之前的学习和实践Hive中，使用的都是CLI或者hive –e的方式，该方式仅允许使用HiveQL执行查询、更新等操作，并且该方式比较笨拙单一。幸好Hive提供了轻客户端的实现，
通过HiveServer或者HiveServer2，客户端可以在不启动CLI的情况下对Hive中的数据进行操作，两者都允许远程客户端使用多种编程语言如Java、Python向Hive提交请求，取回结果。

```
$ hive --service hiveserver2
```

* 后来为了实现metastore的HA服务,在两台机器上启动metastore的服务(150、151)
client配置文件改下:
```
<property>
	<name>hive.metastore.uris</name>
	<value>thrift://10.10.121.150:9083,thrift://10.10.121.151:9083</value>
</property>
```
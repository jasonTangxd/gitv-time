# 重点安装ganglia监控工具
>安装PHP+HTTP  见上。

```
服务端(master):
安装:ganglia-gmetad(ganglia-gmond)+ganglia-web+rrdtool+httpd+php
监控节点端(slave) :
安装:ganglia-gmond
```

## 前期准备
yum加入 epel 源
```
rpm -Uvh http://dl.fedoraproject.org/pub/epel/6/x86_64/epel-release-6-8.noarch.rpm
```
yum加入 remi 源
```
rpm -Uvh http://rpms.famillecollet.com/enterprise/remi-release-6.rpm
```

###### 下载
http://ganglia.info/


安装依赖包
```
>yum install gcc apr apr-devel libconfuse libconfuse-devel expat-devel pcre pcre-devel –y
```

如果需安装gmetad
```
>yum install rrdtool* rrdtool-devel* -y
```

如果需安装gweb
```
>yum install rsync -y
```

异常：``zlib library not configured properly`` 解决：
```
yum install -y zlib-devel
```

``libconfuse not found``(编译安装ganglia-3.7.x步有问题)

libconfuse的安装（自己使用google搜索下载地址）：
```
wget http://savannah.nongnu.org/download/confuse/confuse-2.7.tar.gz
tar -zxvf confuse-2.7.tar.gz
cd confuse-2.7
./configure CFLAGS=-fPIC --disable-nls
make && make install
```

## 安装gmond
```
>tar -zxvf ganglia-3.6.0.tar.gz
>cd ganglia-3.6.0/
>./configure
>make && make install
```

```
>cp gmond/gmond.init /etc/init.d/gmond
>chkconfig --add gmond
```

```
vim /etc/init.d/gmond
```
> 修改如下（find / -name 'gmond'搜索之）：
> GMOND=/usr/local/sbin/gmond

```
>gmond -t > /usr/local/etc/gmond.conf
>mkdir -p /etc/ganglia
>ln -s /usr/local/etc/gmond.conf /etc/ganglia/gmond.conf
```

监控端配置（需要监控的节点均要配置）
```
>vim /etc/ganglia/gmond.conf
```

将cluster选项中 name设置为gmetad中data_source指定的名称即可(hadoop)
```
cluster {
  name = “bi"//集群名称，要与gmetad.conf名称一致
  owner = "unspecified"//集群用户名称
  latlong = "unspecified"
  url = "unspecified"
}

host {
  location = "unspecified"
}

udp_send_channel {
  mcast_join = 239.2.11.71
  port = 8649
  ttl = 1
}

udp_recv_channel {
  mcast_join = 239.2.11.71
  port = 8649
  bind = 239.2.11.71
  retry_bind = true
  # Size of the UDP buffer. If you are handling lots of metrics you really
  # should bump it up to e.g. 10MB or even higher.
  # buffer = 10485760
}
```

``每台机器添加路由:/sbin/route add -host 239.2.11.71 dev eth0``
```
>service gmond start
>chkconfig gmond on
```

## 安装gmetad
#### 安装rrdtool
下载 wget  http://oss.oetiker.ch/rrdtool/pub/rrdtool-1.4.8.tar.gz
```
cd rrdtool-1.4.8
./configure --prefix=/usr/rrdtool
make && make install
```
然后运行
```
/usr/rrdtool/bin/rrdtool
```
``(记得更改ganglia web项目的conf.php里面的rrdtool路径)``

#### 错误汇总：

``Can't locate ExtUtils/MakeMaker.pm in @INC (@INC contains: /usr/local/lib64/perl5 /usr/local/share/perl5 /usr/lib64/perl5/vendor_perl /usr/share/perl5/vendor_perl /usr/lib64/perl5 /usr/share/perl5 .) at Makefile.PL line 1.``

解决：
```
yum install perl-ExtUtils-CBuilder perl-ExtUtils-MakeMaker就可以了
```


错误：
``http://blog.chinaunix.net/uid-25135004-id-3169297.html``

解决：
```
yum install pango-devel*
yum install cairo-devel*
```

#### 正式gmetad:
```
>./configure  CFLAGS="-I/usr/rrdtool/include/" CPPFLAGS="-I/usr/rrdtool/include/" LDFLAGS="-L/usr/rrdtool/lib/" --with-gmetad --enable-gexec --prefix=/data1/ganglia/
>make && make install
```
```
>cp gmetad/gmetad.init /etc/init.d/gmetad
>chkconfig --add gmetad
```
```
>vim /etc/init.d/gmetad（这个是service gmetad start脚本的实现）
```

> 修改如下（find / -name 'gmetad'搜索之）：

```
GMETAD=/usr/local/sbin/gmetad
------------------------------
为维护方便，修改配置路径：
>mkdir -p /etc/ganglia
>ln -s /usr/local/etc/gmetad.conf /etc/ganglia/gmetad.conf
```

```
>vim /etc/ganglia/gmetad.conf
```
```
data_source "bi" slave8.gitv.rack2.bk slave9.gitv.rack2.bk slave10.gitv.rack2.bk slave11.gitv.rack2.bk master2.gitv.rack2.bk master1.gitv.rack1.bk slave1.gitv.rack1.bk slave2.gitv.rack1.bk slave3.gitv.rack1.bk slave4.gitv.rack1.bk slave5.gitv.rack1.bk slave6.gitv.rack1.bk
interactive_port 8653
只需要更改 data_source一行，"hadoop"代表集群的名字，master,slave1,slave2，就是要监控的机器列表。默认端口8649。
```

启动服务
```
>service gmetad start
>chkconfig gmetad on
```
如果启动不起来

``/usr/local/sbin/gmetad -c /etc/ganglia/gmetad.conf -d 1``

打出的日志提示rrds的所属用户需要是nobody才能启动ganglia的gmetad
```
chown -R nobody:nobody /data1/ganglia/rrds/
```

在gmetad机器上运行 tcpdump -i eth0 udp port 8649，或者telnet localhost 8649

## 安装Ganglia Web
```
>tar -zxvf ganglia-web-3.5.12.tar.gz
>cd ganglia-web-3.5.12/
```
```
>vim Makefile
```

> GDESTDIR =  /work/installed/apache/htdocs/ganglia  <ganglia-web 安装目录链接到httpd 主站点目录(apache服务器访问的根目录)>
> APACHE_USER =  daemon

```
>make && make install
cd /work/installed/apache/htdocs/ganglia/
cp conf_default.php conf.php
vim conf.php:
$conf['ganglia_port'] = 8653;
gmetad_root=/data1/ganglia
```

其他看着弄，rrdtool路径/usr/rrdtool/bin/rrdtool，/data1/ganglia/rrds路径等

## 访问Ganglia Web
服务端本机访问：http//127.0.0.1:10086/ganglia
呈现如下界面：（截图客户端访问）

``单播``意思是
> 多个gmond通过udp发送数据给一个特定的gmond,然后gmetad通过tcp和这台gmond请求响应。所以此时gmetad的配置中只需要写localhost

``多播``意思是
> 所有的gmond通过广播互相udp发送、接受数据->缓存 gmetad通过pull监控下某台机器的数据存入rrd

## 后续
后来发现gmetad的那台机器的io非常的搞，近乎100%，解决方案：
```
启动rrdtool自带的rrdcache服务： /usr/rrdtool/bin/rrdcached
修改/work/installed/apache/htdocs/ganglia/conf.php:
```

后续1.1：
```
/usr/rrdtool/bin/rrdcached
```

ps:这样并没有什么卵用,启动方式不对

先service xx start启动rrdcached,再启动gmetad
rrdcached 自己做服务，配置：
```
vim /etc/default/rrdcached
```

```
RUN_RRDCACHED=1
RRDCACHED_USER="nobody"
OPTS="-b /data/ganglia/rrds  -w 300 -z 300"
PIDFILE="/var/run/rrdcached/rrdcached.pid"
SOCKFILE="/var/run/rrdcached/rrdcached.sock"
SOCKPERMS=0660
```

添加配置：
```
vim /etc/init.d/gmetad
```

```
export RRDCACHED_ADDRESS=unix:/var/run/rrdcached/rrdcached.sock
```

```
vim /work/installed/apache/htdocs/ganglia/conf.php
```

```
$conf['rrdcached_socket'] = "unix:/var/run/rrdcached/rrdcached.sock";
```


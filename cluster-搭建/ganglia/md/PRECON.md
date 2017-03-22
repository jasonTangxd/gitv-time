## 1.安装apche-httpd服务(source编译)
linux下，默认安装软件，在开发过程中会经常不知道到什么路径下找到所需的程序，因此手动安装程序对后续的使用会提供非常大的便利。在此，以安装apache为例。

#### 一.准备
* 1.安装 apr

下载地址：http://apr.apache.org/download.cgi
```
            [root@ubuntu:/work/soft/apache]# tar jxvf apr-1.5.0.tar.bz2
            [root@@ubuntu:/work/soft/apache/apr-1.5.0]# ./configure --prefix=/work/installed/apr
            [root@@ubuntu:/work/soft/apache/apr-1.5.0]# make && make install
```
* 2.安装 apr-util

下载地址：http://apr.apache.org/download.cgi
```
            [root@ubuntu:/work/soft/apache]# tar jxvf  apr-util-1.5.3.tar.bz2
            [root@@ubuntu:/work/soft/apache/apr-util-1.5.3]# ./configure --prefix=/work/installed/apr-util --with-apr=/work/installed/apr
            [root@@ubuntu:/work/soft/apache/apr-util-1.5.3]# make && make install
```

* 3.安装 pcre

下载地址：http://pcre.org/
```
[root@ubuntu:/work/soft/apache]# tar jxvf  pcre-8.35.tar.bz2
[root@@ubuntu:/work/soft/apache/pcre-8.35]# ./configure --prefix=/work/installed/pcre
[root@@ubuntu:/work/soft/apache/pcre-8.35]# make && make install
```
注意：如果在安装 pcre 时，遇到问题：configure: error: You need a C++ compiler for C++ support.
解决方法：sudo apt-get install build-essential

#### 二.安装apache

下载地址：http://httpd.apache.org/
```
[root@ubuntu:/work/soft/apache]# tar jxvf  httpd-2.4.9.tar.bz2
[root@@ubuntu:/work/soft/apache/httpd-2.4.9]#  ./configure --prefix=/work/installed/apache --with-apr=/work/installed/apr --with-apr-util=/work/installed/apr-util --with-pcre=/work/installed/pcre
[root@@ubuntu:/work/soft/apache/httpd-2.4.9]# make && make install
```

#### 三.配置
* 1.修改配置文件,否则会出现问题：

``AH00558: httpd: Could not reliably determine the server's fully qualified domain name, using 127.0.1.1. Set the 'ServerName' directive globally to suppress this message``

解决方法：
```
[root@ubuntu:/work/installed/apache]# vim /work/installed/apache/conf/httpd.conf
```

``把：# ServerName www.example.com:80 改为：ServerName localhost:80``

* 2.启动
```
[root@ubuntu:/work/installed/apache]# ./bin/apachectl start
```

* 3.开机启动

打开文件：/etc/rc.local
```
[root@ubuntu:/]#vim /etc/rc.local
```
> 添加：/work/installed/apache/bin/apachectl start

#### 四.参考文件
1.http://blog.csdn.net/chenxiaohua/article/details/2047757

2.http://blog.163.com/hlz_2599/blog/static/142378474201182811611382/

## 2.安装编译php源码包
#### 1.安装PHP

* 获得PHP源码：php-5.4.1.tar.gz（最新版本为5.4.2），保存到/usr/local目录下

下载地址：http://cn.php.net/get/php-5.4.2.tar.gz/from/a/mirror

* 解压缩源码文件：
```
#tar -zvxf php-5.4.1.tar.gz
```

* 安装libxm12以及libxml2-devel，不然编译源码时会出现“Configure: error: xml2-config not found. Please check your libxml2 installation.”问题
```
# yum install -y libxml2* libxm12-devel*
```

* 编译源码：
```
#cd /usr/local/php-5.4.1
find / -name apxs  or find / -name apxs2
```

看哪个存在，都不存在则:

apxs 安装
```
# rpm -ivh apr-devel-0.9.4-24.9.i386.rpm
# rpm -ivh apr-util-devel-0.9.4-21.i386.rpm
# rpm -ivh pcre-devel-4.5-4.el4_5.1.i386.rpm
# rpm -ivh httpd-devel-2.0.52-38.ent.centos4.i386.rpm
```
确认
```
# find / -name apxs
/usr/sbin/apxs
./configure --prefix=/usr/local/php --with-apxs= /usr/sbin/apxs
#make
```
(源码编译的httpd服务不存在这个问题)
```
# ./configure --prefix=/usr/local/php --with-apxs2=/work/installed/apache/bin/apxs
```

* 安装
#make && make install

#### 2.配置PHP
* 将PHP源码包（/usr/local/php-5.4.1）中的php.ini-development文件复制到/usr/local/lib/下，更名为php.ini。
```
#cp ./php.ini-development /usr/local/lib/php.ini
```

* 修改Apache配置文件
```
vim /work/installed/apache/conf/httpd.conf
```
> 以支持对PHP的解析。如果httpd.conf中没有下列语句，就将它们分别添加到LoadModule和AddType项的后面。
```
LoadModule php5_module modules/libphp5.so
AddType application/x-httpd-php .php
在DirectoryIndex index.html index.html.var一行后加入index.php，即改为：
DirectoryIndex index.html index.html.var index.php
```

重启Apache服务器：

```
#./bin/apachectl restart
```
* 测试PHP：
在Apache服务器的文件根目录（ /work/installed/apache/htdocs）下新建一个PHP文件test.php，并输入以下内容：
```
<?php
     phpinfo();
?>
```

在浏览器中输入http://localhost/test.php
如果看到下图，则表示已成功安装了PHP。


## 3.Apache-http对php的支持
配置相关参数
```
vi /work/installed/apache/conf/httpd.conf
```
对apache做如下配置:
```
#将ServerAdmin mailto:linux@linuxidc.com一行改为您的邮箱地址
#DocumentRoot "/usr/local/apache2/htdocs/" 此处为html文件主目录
#Options FollowSymLinks MultiViews 为安全起见，去掉"Indexes"
# DirectoryIndex index.html index.php
#设置apache的默认文件名次序
#AddType application/x-httpd-php .php .phtml .php3 .inc
#AddType application/x-httpd-php-source .phps
#设置php文件后缀
```
存盘退出

参考上面［测试test.php］之前的整合为主（经测试可用）
```
vi /usr/local/lib/php.ini
#register-golbals = On（没有改，可用）
```
## 依赖下载
* automake-1.12.1.tar.gz 包下载地址
http://ftp.gnu.org/gnu/automake/

* autoconf-2.69.tar.gz 包下载地址
http://ftp.gnu.org/gnu/autoconf

* libtool-2.2.4.tar.gz 包下载地址
http://ftp.gnu.org/gnu/libtool/

* twemproxy-master.zip 包下载地址
http://ftp.gnu.org/gnu/twemproxy/

## 各依赖安装
下载完成之后，依次解压，并安装。（严格依次按以下的顺序解压文件，否则安装失败）
```tar -xf autoconf-2.69.tar.gz
./configure
make && make install
```
```
tar -xf automake-1.12.1.tar.gz
./configure
make && make install
```
```
tar -xf libtool-2.2.4.tar.gz
./configure
make && make install
```
```
unzip twemproxy-master.zip
cd twemproxy-master
autoreconf -ivf
./configure --prefix=/data/opt/soft/twemproxy-1.3.20
make && make install
```

## 问题以及解决
在安装过程中可能会碰到以下问题（均是包的依赖问题）：

1、解压twemproxy-master后，cd twemproxy-master，./configure 报错，这是因为要先执行
```
autoreconf -ivf
```

2、执行autoreconf -ivf，提示

```
autoreconf: Entering directory `.'
autoreconf: configure.ac: not using Gettext
autoreconf: running: aclocal --force -I m4
Can't exec "automake": No such file or directory at /usr/local/share/autoconf/Autom4te/FileUtils.pm line 326, <GEN2> line 7.
autoreconf: failed to run automake: No such file or directory
```

``解决方法是安装完autoconf包之后，再安装automake包就可以了``

3、执行autoreconf -ivf，提示
```
[html] view plain copy
configure.ac:36: error: possibly undefined macro: AC_PROG_LIBTOOL
```
``解决方法是安装完autoconf包和automake包之后，再安装libtool包就可以了``
## 前期准备
#### pip安装
下载pip安装包
```
wget "https://pypi.python.org/packages/source/p/pip/pip-1.5.4.tar.gz#md5=834b2904f92d46aaa333267fb1c922bb" --no-check-certificate
```
相关依赖
```
yum -y install python-setuptools
yum -y install python-devel
```
安装
```
 tar -xzvf pip-1.5.4.tar.gz
 cd pip-xx
 python setup.py install
```
## WEB搭建
#### django安装
下载源码包：https://www.djangoproject.com/download/

python2.6支持Django1.6以及之前的
```
tar xzvf Django-1.6.tar.gz
cd Django-1.6
python setup.py install
-----
pip install Django
```

#### 测试web项目启动
* 新建web项目:
```
django-admin.py startproject monitor_center
```

* 写个简单的url映射
```
 urlpatterns = [
    url(r'^admin/', include(admin.site.urls)),
    url(r'^collect_message/$', message.collect_message),
]
```

```
def collect_message(request):
    if request.method == 'GET':
        return HttpResponse("<h1>你好啊</h1>")
```
>进入项目root目录下

```
cd ~/Project/idea/gitv-kang/dailyjob-parent/system-monitor/monitor_cent
```
* 先执行如下命令<#1.6+的命令>
```
python manage.py migrate
```
```
Operations to perform:
  Synchronize unmigrated apps: staticfiles, messages
  Apply all migrations: admin, contenttypes, auth, sessions
Synchronizing apps without migrations:
  Creating tables...
    Running deferred SQL...
  Installing custom SQL...
Running migrations:
  Rendering model states... DONE
  Applying contenttypes.0001_initial... OK
  Applying auth.0001_initial... OK
  Applying admin.0001_initial... OK
  Applying contenttypes.0002_remove_content_type_name... OK
  Applying auth.0002_alter_permission_name_max_length... OK
  Applying auth.0003_alter_user_email_max_length... OK
  Applying auth.0004_alter_user_username_opts... OK
  Applying auth.0005_alter_user_last_login_null... OK
  Applying auth.0006_require_contenttypes_0002... OK
  Applying sessions.0001_initial... OK
```
* 启动项目服务
```
python manage.py runserver
```

```
Performing system checks...

System check identified no issues (0 silenced).
December 13, 2016 - 05:47:45
Django version 1.8.4, using settings 'info_metad.settings'
Starting development server at http://127.0.0.1:8000/
Quit the server with CONTROL-C.
OK
[13/Dec/2016 05:47:50] "GET /collect_message/ HTTP/1.1" 500 59151
```

* 建立app(model.py、admin.py)
```
python manage.py startapp monitor_app
```

* app配置(上一步后执行!)
>vim settings.py
```
INSTALLED_APPS = (
     'django.contrib.admin',
     'django.contrib.auth',
     'django.contrib.contenttypes',
     'django.contrib.sessions',
     'django.contrib.messages',
     'django.contrib.staticfiles',
     'monitor_app',
 )
```

* 用下面的命令验证model的有效性：
```
python manage.py validate
```

* create table show:
```
  python manage.py sqlall monitor_app
```

假如出错如下(django1.6+会出错):
```
CommandError: App 'monitor_app' has migrations. Only the sqlmigrate and sqlflush commands can be used when an app has migrations
```
执行:
```
python manage.py makemigrations
```

>然后你就可以看见各种SQL语句了,看上去不错

>但是sqlall 命令并没有在数据库中真正创建数据表

>只是把SQL语句段打印出来,这样你可以看到Django究竟会做些什么

* 真正创建表
```
python manage.py syncdb
```

```
Django1.6中默认就会找各个app的templates下的模板文件，所以你不需要做另外的设置。
只需要你去掉关于TEMPLATES_DIR的设置就可以了
```

## py模块依赖安装

#### 安装psutil模块
>开始下载的tar包,setup.py install单独不好使,故安装了pip

下载地址: https://pypi.python.org/pypi/psutil/
```
tar -xzvf psutil-3.2.2.tar.gz
cd psutil-3.2.2
python setup.py install
```
```
pip install psutil
```
#### cassandra-driver
* 前提依赖
```
yum install python-devel mysql-devel zlib-devel openssl-devel
yum -y install python-setuptools
```

* cython

``需要版本0.20.x-0.25.x``

下载地址：https://github.com/cython/cython/tree/0.22.x

```
cd cython-0.22.x/
python setup.py install
```

* future

下载地址： https://pypi.python.org/simple/futures/
(下载最下面的futures-2.1.4.tar.gz)
```
tar -xvf futures-2.1.4.tar
cd futures-2.1.4
python setup.py install
```

* cassandra-driver

下载地址: https://pypi.python.org/pypi/cassandra-driver

```
cd cassandra-driver-3.7.1/
```
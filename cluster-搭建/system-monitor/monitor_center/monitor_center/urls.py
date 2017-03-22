# coding=utf-8
import os

from django.conf.urls import patterns, include, url
from django.contrib import admin

import view.collect_message as message

admin.autodiscover()
BASE_DIR = os.path.dirname(os.path.dirname(__file__))
JS_DIR = os.path.join(BASE_DIR, 'templates/js')
WELCOME_DIR = os.path.join(BASE_DIR, 'templates/welcome_inbox/')
FIRETEXT_DIR = os.path.join(BASE_DIR, 'templates/fire_text/')
urlpatterns = patterns('',
                       # Examples:
                       url(r'^admin/', include(admin.site.urls)),
                       # url(r'^$', 'monitor_center.views.home', name='home'),
                       # url(r'^blog/', include('blog.urls')),
                       url(r'^$', message.index),
                       url(r'^choose$', message.choose),
                       url(r'^collect_message/$', message.collect_message_redis),
                       url(r'^showHistoryPage/$', message.show_history_page),
                       url(r'^showNowPage/$', message.show_redis_data),
                       # 访问的静态url的映射本地路径关系
                       url(r'^js/(?P<path>.*)$', 'django.views.static.serve',
                           {'document_root': JS_DIR}),
                       url(r'^welcome_inbox/(?P<path>.*)$', 'django.views.static.serve',
                           {'document_root': WELCOME_DIR}),
                       url(r'^fire_text/(?P<path>.*)$', 'django.views.static.serve',
                           {'document_root': FIRETEXT_DIR}),
                       )

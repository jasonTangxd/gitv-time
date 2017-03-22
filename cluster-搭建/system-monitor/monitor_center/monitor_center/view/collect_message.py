# coding=utf-8

import redis
from django.http.response import HttpResponse
from django.shortcuts import render_to_response

redis_port = 6379
redis_host = "127.0.0.1"
redis_pool = redis.ConnectionPool(host=redis_host, port=redis_port)


def get_ip(request):
    if request.META.has_key('HTTP_X_FORWARDED_FOR'):
        ip = request.META['HTTP_X_FORWARDED_FOR']
    else:
        ip = request.META['REMOTE_ADDR']
    return ip


def index(request):
    return render_to_response('index.html')


def choose(request):
    if request.method == 'GET':
        host = request.REQUEST.get('host')
        return render_to_response('choose.html', {'host': host})
    else:
        return HttpResponse("<h1>请不要手动请求!!!</h1>")


def collect_message_redis(request):
    if request.method == 'POST':
        json_dict = {}
        for key in request.POST:
            value_list = request.POST.getlist(key)
            json_dict[key] = value_list
        redis_key = get_ip(request)
        redis_value = str(json_dict)
        redis_connect = redis.Redis(connection_pool=redis_pool)
        redis_connect.set(redis_key, redis_value)
        return HttpResponse('ok')
        # return render_to_response('json_show.html', {'tb': table})
    else:
        return HttpResponse("<h1>请使用post请求!!!</h1>")


def show_redis_data(requset):
    return HttpResponse("开发中。。。。")


def show_history_page(request):
    return render_to_response('show_history_page.html')

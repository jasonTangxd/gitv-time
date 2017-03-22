# -*- coding:utf-8 -*-
import redis
import sys

type = sys.argv[1]
listen_port = int(sys.argv[2])
mac_host = sys.argv[3]
compRedis = None
macRedis = None
if type == 'vod':
    macRedis = redis.Redis(host=mac_host, port=listen_port)
    compRedis = redis.Redis(host='10.10.121.120', port=55555)
elif type == 'liv':
    macRedis = redis.Redis(host=mac_host, port=listen_port)
    compRedis = redis.Redis(host='10.10.121.120', port=55556)
else:
    print 'args is error'


def comp(channel_partner):
    with compRedis.pipeline() as pipe:
        try:
            values = channel_partner.split('|')
            channel = values[0]
            partner = values[1]
            total_key = 'total|' + channel
            partner_key = 'partner|' + partner + '|' + channel
            pipe.watch(total_key, partner_key)
            if pipe.get(total_key) > 0 and pipe.get(partner_key) > 0:
                pipe.multi()
                pipe.decr(total_key)
                pipe.decr(partner_key)
                pipe.execute()
        except Exception as ex:
            print ex
            return


            # python xx.py vod 55555 10.10.121.148


if __name__ == "__main__":
    # 开始监听
    ps = macRedis.pubsub()
    ps.psubscribe("__keyevent@0__:expired")
    for item in ps.listen():
        if item['type'] == 'pmessage':
            mac = str(item['data'])  # 获得ttl到期后删除的mac
            macCopy = mac + '|cp'
            with macRedis.pipeline() as pipe:
                try:
                    pipe.watch(mac, macCopy)  # 监视的key
                    channel_partner = pipe.get(macCopy)  # 获取存在的key值
                    if channel_partner:
                        pipe.multi()  # 开启事务
                        pipe.delete(macCopy)
                        pipe.execute()
                        comp(channel_partner)
                except Exception as ex:
                    continue

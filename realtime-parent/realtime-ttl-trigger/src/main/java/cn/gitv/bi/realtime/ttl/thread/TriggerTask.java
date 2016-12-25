package cn.gitv.bi.realtime.ttl.thread;

import cn.gitv.bi.realtime.ttl.utils.StringHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.util.List;

/**
 * Created by Kang on 2016/12/24.
 */
public class TriggerTask implements Runnable {
    private static Logger log = LoggerFactory.getLogger(TriggerTask.class);
    private JedisPool compJedisPool = null;
    private JedisPool jedisPool = null;
    private String mac = null;

    public TriggerTask(JedisPool compJedisPool, JedisPool jedisPool, String mac) {
        this.compJedisPool = compJedisPool;
        this.jedisPool = jedisPool;
        this.mac = mac;
    }

    @Override
    public void run() {
        Jedis macJedis = null;
        Jedis comp = null;
        try {
            macJedis = jedisPool.getResource();
            log.info("get ttl is {}", mac);
            String macCopy = mac + "|cp";
            macJedis.watch(mac, macCopy);
            String channelPartner = macJedis.get(macCopy);
            Transaction multi = macJedis.multi();
            multi.del(macCopy);
            List<Object> result = multi.exec();
            if (result == null || result.isEmpty()) {
                log.warn("transaction is disturbed,and mac is {}", mac);
                return;
            }
            if (channelPartner != null) {
                List<String> cpList = StringHandle.str_split(channelPartner);
                String channel = cpList.get(0);
                String partner = cpList.get(1);
                String totalKey = "total|" + channel;
                String partnerKey = "partner|" + partner + '|' + channel;
                comp = compJedisPool.getResource();
                comp.decr(totalKey);
                comp.decr(partnerKey);
                log.info("the mac {} is from redis-ttl,and excute it!", mac);
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            if (macJedis != null) {
                macJedis.close();
            }
            if (comp != null) {
                comp.close();
            }
        }
    }
}

package cn.gitv.bi.rtvod.usercount.utils;

import com.google.common.hash.Hashing;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RedisChoose {
    private static final String[] redisHosts = {"10.10.121.151", "10.10.121.152", "10.10.121.153", "10.10.121.138", "10.10.121.139", "10.10.121.148", "10.10.121.149", "10.10.121.150"};
    private static final Map<Integer, JedisPool> jedisMap = new ConcurrentHashMap<>();

    static {
        int count = 0;
        for (String host : redisHosts) {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxIdle(20);
            config.setTestOnBorrow(true);
            JedisPool pool1 = new JedisPool(config, host, 55555);
            jedisMap.put(count, pool1);
            count++;
            JedisPool pool2 = new JedisPool(config, host, 55556);
            jedisMap.put(count, pool2);
            count++;
        }
    }

    public static Jedis macHash4Redis(long mac) {
        int hashCode = Hashing.consistentHash(mac, jedisMap.size());
        JedisPool jedisPool = jedisMap.get(hashCode);
        return jedisPool.getResource();
    }
}

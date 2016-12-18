package cn.gitv.bi.rtliv.usercount.utils;

import com.google.common.hash.Hashing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RedisChoose {
    private static final String[] redisHosts = {"10.10.121.151", "10.10.121.152"};
    private static final Map<Integer, JedisPool> jedisMap = new ConcurrentHashMap<>();

    static {
        int count = 0;
        for (String host : redisHosts) {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxIdle(20);
            config.setTestOnBorrow(true);
            JedisPool pool1 = new JedisPool(config, host, 55557);
            jedisMap.put(count, pool1);
            count++;
            JedisPool pool2 = new JedisPool(config, host, 55558);
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

package cn.gitv.bi.rtvod.usercount.utils;

import com.google.common.hash.Hashing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RedisChoose1 {
    public static Logger log = LoggerFactory.getLogger(RedisChoose1.class);
    private static final String[] redisHosts = {"10.10.121.151", "10.10.121.152", "10.10.121.153", "10.10.121.138", "10.10.121.139", "10.10.121.148", "10.10.121.149", "10.10.121.150"};
    private Map<Integer, JedisPool> macHashJedisMap = new HashMap<>();
    private Map<JedisPool, Set<String>> macMget2PoolMap = new HashMap<>();

    public RedisChoose1() {
        int count = 0;
        for (String host : redisHosts) {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxIdle(20);
            config.setTestOnBorrow(true);
            JedisPool pool1 = new JedisPool(config, host, 55557);
            macHashJedisMap.put(count, pool1);
            count++;
            JedisPool pool2 = new JedisPool(config, host, 55558);
            macHashJedisMap.put(count, pool2);
            count++;
        }
    }

    public synchronized void activateTTL() {
        for (Map.Entry<JedisPool, Set<String>> item : macMget2PoolMap.entrySet()) {
            Jedis jedis = item.getKey().getResource();
            Set<String> macOnceSet = item.getValue();
            String[] macGetArray = new String[macOnceSet.size()];
            macOnceSet.toArray(macGetArray);
            jedis.mget(macGetArray);
            macOnceSet.clear();
        }
    }

    public synchronized Jedis macHash2Redis(String mac) {
        long macLong = 0;
        try {
            macLong = Long.parseLong(mac.replaceAll(":", ""), 16);
        } catch (NumberFormatException e) {
            log.error("", e);
        }
        /*如果传进来的mac地址不能合法转成long类型，默认返回0的pool*/
        int hashCode = Hashing.consistentHash(macLong, macHashJedisMap.size());
        JedisPool jedisPool = macHashJedisMap.get(hashCode);
        if (macMget2PoolMap.containsKey(jedisPool)) {
            Set<String> macSet = macMget2PoolMap.get(jedisPool);
            macSet.add(mac);
            macMget2PoolMap.put(jedisPool, macSet);
        } else {
            Set<String> macSet = new HashSet<>();
            macSet.add(mac);
            macMget2PoolMap.put(jedisPool, macSet);
        }
        return jedisPool.getResource();
    }
}

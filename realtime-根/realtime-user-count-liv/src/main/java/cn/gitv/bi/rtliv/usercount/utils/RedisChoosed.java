//package cn.gitv.bi.rtliv.usercount.utils;
//
//import com.google.common.hash.Hashing;
//import redis.clients.jedis.Jedis;
//import redis.clients.jedis.JedisPool;
//import redis.clients.jedis.JedisPoolConfig;
//
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//
//public class RedisChoosed {
//    private static final String[] redisHosts = {"10.10.121.151", "10.10.121.152"};
//    private Map<Integer, JedisPool> jedisMap = new HashMap<>();
//    private Map<JedisPool, Set<String>> pool2MacSetMap = new HashMap<>();
//
//    public RedisChoosed() {
//        int count = 0;
//        for (String host : redisHosts) {
//            JedisPoolConfig config = new JedisPoolConfig();
//            config.setMaxIdle(20);
//            config.setTestOnBorrow(true);
//            JedisPool pool1 = new JedisPool(config, host, 55557);
//            jedisMap.put(count, pool1);
//            count++;
//            JedisPool pool2 = new JedisPool(config, host, 55558);
//            jedisMap.put(count, pool2);
//            count++;
//        }
//    }
//
//    public synchronized Jedis macHash4Redis(String mac) {
//        long macLong = Long.parseLong(mac.replaceAll(":", ""), 16);
//        int hashCode = Hashing.consistentHash(macLong, jedisMap.size());
//        JedisPool jedisPool = jedisMap.get(hashCode);
//        if (pool2MacSetMap.containsKey(jedisPool)) {
//            Set<String> macSet = pool2MacSetMap.get(jedisPool);
//            macSet.add(mac);
//            pool2MacSetMap.put(jedisPool, macSet);
//        } else {
//            Set macSet = new HashSet();
//            macSet.add(mac);
//            pool2MacSetMap.put(jedisPool, macSet);
//        }
//        return jedisPool.getResource();
//    }
//
//    public synchronized void cronMget() {
//        for (Map.Entry<JedisPool, Set<String>> item : pool2MacSetMap.entrySet()) {
//            JedisPool jedisPool = item.getKey();
//            Set<String> macSet = item.getValue();
//            String[] macSetArray = macSet.toArray(new String[macSet.size()]);
//            jedisPool.getResource().mget(macSetArray);
//        }
//        pool2MacSetMap.clear();
//    }
//}

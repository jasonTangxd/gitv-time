package cn.gitv.bi.rtliv.usercount.bolts;

import cn.gitv.bi.rtliv.usercount.utils.StringHandle;
import org.apache.storm.Config;
import org.apache.storm.Constants;
import org.apache.storm.redis.bolt.AbstractRedisBolt;
import org.apache.storm.redis.common.config.JedisPoolConfig;
import org.apache.storm.shade.org.jboss.netty.util.internal.ConcurrentHashMap;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCommands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserCount extends AbstractRedisBolt {
    private static final long serialVersionUID = 1L;
    public static Logger log = LoggerFactory.getLogger(UserCount.class);
    public Map<String, Integer> cacheMap;

    public UserCount(JedisPoolConfig config) {
        super(config);

    }

    @SuppressWarnings("rawtypes")
    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector collector) {
        super.prepare(map, topologyContext, collector);
        cacheMap = new ConcurrentHashMap<>();
    }

    public void execute(Tuple input) {
        JedisCommands jedisCommands = null;
        try {
            jedisCommands = getInstance();
            if (isTickTuple(input)) {
                SecondsToRedis(jedisCommands);
            } else {
                mainTain(jedisCommands, input);
            }
            collector.ack(input);
        } catch (Exception e) {
            log.error("", e);
            collector.fail(input);
        } finally {
            if (jedisCommands != null) {
                returnInstance(jedisCommands);
            }
        }
    }

    private void mainTain(JedisCommands jedisCommands, Tuple input) {
        String ADD = input.getStringByField("ADD");
        String DEC = input.getStringByField("DEC");
        if (ADD != null) {
            List<String> add = StringHandle.str_split(ADD);
            String channel = add.get(0);
            String partner = add.get(1);
            String totalChannel = StringHandle.str_join("total", channel);// total|cctv-1
            String partnerChannel = StringHandle.str_join("partner", partner, channel);// partner|cmcc|cctv-1
            CacheInMapAdd(totalChannel);
            CacheInMapAdd(partnerChannel);
        }
        if (DEC != null) {
            List<String> des = StringHandle.str_split(DEC);
            String channel = des.get(0);
            String partner = des.get(1);
            String totalChannel = StringHandle.str_join("total", channel);// total|cctv-1
            String partnerChannel = StringHandle.str_join("partner", partner, channel);// partner|cmcc|cctv-1
            CacheInMapDec(totalChannel);
            CacheInMapDec(partnerChannel);
        }
    }


    private void CacheInMapAdd(String key) {
        if (cacheMap.containsKey(key)) {
            cacheMap.put(key, cacheMap.get(key) + 1);
        } else {
            cacheMap.put(key, 1);
        }
    }

    private void CacheInMapDec(String key) {
        if (cacheMap.containsKey(key)) {
            cacheMap.put(key, cacheMap.get(key) - 1);
        } else {
            cacheMap.put(key, -1);
        }
    }

    public static boolean isTickTuple(Tuple tuple) {
        return tuple.getSourceComponent().equals(Constants.SYSTEM_COMPONENT_ID) && tuple.getSourceStreamId().equals(
                Constants.SYSTEM_TICK_STREAM_ID);
    }

    private void SecondsToRedis(JedisCommands jedis) {
        if (cacheMap.size() == 0) {
            return;
        } else {
            for (Map.Entry<String, Integer> item : cacheMap.entrySet()) {
                String key = item.getKey();
                int value = item.getValue();
                jedis.incrBy(key, value);
            }
            cacheMap.clear();
        }

    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        Map<String, Object> conf = new HashMap<String, Object>();
        conf.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, 1);
        return conf;
    }

    public void cleanup() {
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }

}

package cn.gitv.bi.rtliv.usercount.bolts;

import cn.gitv.bi.rtliv.usercount.constant.Constant;
import cn.gitv.bi.rtliv.usercount.utils.StringHandle;
import org.apache.storm.redis.bolt.AbstractRedisBolt;
import org.apache.storm.redis.common.config.JedisPoolConfig;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCommands;

import java.util.List;
import java.util.Map;

/**
 * @author likang
 */
public class AnRecord extends AbstractRedisBolt {
    private static final long serialVersionUID = 1L;
    public static Logger log = LoggerFactory.getLogger(AnRecord.class);

    public AnRecord(JedisPoolConfig config) {
        super(config);

    }

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector collector) {
        super.prepare(map, topologyContext, collector);
    }

    public void execute(Tuple input) {
        JedisCommands jedis = null;
        try {
            //jedis实例
            jedis = getInstance();
            //维护AN信息操作redis
            mainTain(jedis, input);
            collector.ack(input);
        } catch (Exception e) {
            log.error("AnRecord: execute exception-->{}", e.getMessage());
            collector.fail(input);
        } finally {
            if (jedis != null) {
                returnInstance(jedis);
            }
        }
    }

    /**
     * param redis
     */
    private void mainTain(JedisCommands redis, Tuple input) {
        String NEW = input.getStringByField("NEW");
        String OLD = input.getStringByField("OLD");
        if (NEW != null) {
            List<String> news = StringHandle.str_split(NEW);
            String channel = news.get(0);
            String an = news.get(1);
            String an_flag = news.get(2);
            String partner = news.get(3);
            String total_channel = StringHandle.str_join("total", channel);// total|cctv-1
            String partner_channel = StringHandle.str_join("partner", partner, channel); // partner|cmcc|cctv-1
            if (Constant.AN_USE.equals(an_flag)) {
                redis.set(total_channel, an);
                redis.set(partner_channel, an);
            }
        }
        if (OLD != null) {
            List<String> olds = StringHandle.str_split(OLD);
            String channel = olds.get(0);
            String an = olds.get(1);
            String an_flag = olds.get(2);
            String partner = olds.get(3);
            String total_channel = StringHandle.str_join("total", channel);// total|cctv-1
            String partner_channel = StringHandle.str_join("partner", partner, channel); // partner|cmcc|cctv-1
            if (Constant.AN_USE.equals(an_flag)) {
                redis.set(total_channel, an);
                redis.set(partner_channel, an);
            }
        }
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

    public void cleanup() {
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }

}

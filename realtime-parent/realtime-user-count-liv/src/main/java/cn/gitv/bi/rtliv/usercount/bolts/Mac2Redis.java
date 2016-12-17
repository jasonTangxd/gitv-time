package cn.gitv.bi.rtliv.usercount.bolts;

import cn.gitv.bi.rtliv.usercount.constant.Constant;
import cn.gitv.bi.rtliv.usercount.utils.RedisChoose;
import cn.gitv.bi.rtliv.usercount.utils.StringHandle;
import org.apache.commons.lang.StringUtils;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.Map;

public class Mac2Redis implements IRichBolt {
    private static final long serialVersionUID = 3099879620406165967L;
    public static Logger log = LoggerFactory.getLogger(Mac2Redis.class);
    private OutputCollector collector = null;

    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        Jedis jedis = null;
        try {
             /*mac转换成标准long*/
            String mac = input.getStringByField("mac");
            long macLong = Long.parseLong(mac.replaceAll(":", ""), 16);
            /*根据mac获得一个redis连接*/
            jedis = RedisChoose.macHash4Redis(macLong);
            checkMacAndHandle(jedis, input);
            collector.ack(input);
        } catch (Exception e) {
            log.error("", e);
            collector.fail(input);
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    private void checkMacAndHandle(Jedis jedis, Tuple input) {
        String partner = input.getStringByField("partner");
        String mac = input.getStringByField("mac");
        String an = input.getStringByField("an");
        String pt = input.getStringByField("pt");
        String vt = input.getStringByField("vt");
        String channelCode = input.getStringByField("channelCode");
        String macCopy = StringHandle.str_join(mac, "cp");
        String channelPartner = StringHandle.str_join(channelCode, partner);
        /*上锁监控*/
        jedis.watch(mac, macCopy);
        switch (pt) {
            case "0": {// 表示开始播放
                String anFlag = null;
                if ("3".equals(vt)) {
                    anFlag = Constant.AN_USE;
                } else {
                    anFlag = Constant.AN_PASS;
                }
                String macCopyV0 = jedis.get(macCopy);
                if (!StringUtils.isBlank(macCopyV0)) {
                    String oldChannel = StringHandle.str_split(macCopyV0).get(0);
                    Transaction transaction = jedis.multi();
                    transaction.set(mac, "0");
                    transaction.expire(mac, Constant.TIME_TTL);
                    transaction.set(macCopy, channelPartner);
                    List<Object> result = transaction.exec();
                    if (result == null || result.isEmpty()) {
                        log.warn("transaction is disturbed,and mac is {},try again...", mac);
                        return;
                    }
                    String ADD = StringHandle.str_join(channelCode, partner);
                    String DEC = StringHandle.str_join(oldChannel, partner);
                    collector.emit(Constant.USER_COUNT, input, new Values(ADD, DEC));
                    //
                    String NEW = StringHandle.str_join(channelCode, an, anFlag, partner);
                    String OLD = StringHandle.str_join(oldChannel, an, Constant.AN_PASS, partner);
                    collector.emit(Constant.AN_RECORD, input, new Values(NEW, OLD));
                } else {
                    Transaction transaction = jedis.multi();
                    transaction.set(mac, "0");
                    transaction.expire(mac, Constant.TIME_TTL);
                    transaction.set(macCopy, channelPartner);
                    List<Object> result = transaction.exec();
                    if (result == null || result.isEmpty()) {
                        log.warn("transaction is disturbed,and mac is {}", mac);
                        return;
                    }
                    String ADD = StringHandle.str_join(channelCode, partner);
                    collector.emit(Constant.USER_COUNT, input, new Values(ADD, null));
                    //
                    String NEW = StringHandle.str_join(channelCode, an, anFlag, partner);
                    collector.emit(Constant.AN_RECORD, input, new Values(NEW, null));
                }
                break;
            }
            case "1":
            case "2": {
                String macCopyV1 = jedis.get(macCopy);
                if (StringUtils.isBlank(macCopyV1)) {
                    return;
                } else {
                    String oldChannel = StringHandle.str_split(macCopyV1).get(0);
                    // 清空这个key
                    Transaction transaction = jedis.multi();
                    transaction.del(macCopy);
                    transaction.del(mac);
                    List<Object> result = transaction.exec();
                    if (result == null || result.isEmpty()) {
                        log.warn("transaction is disturbed,and mac is {}", mac);
                        return;
                    }
                    String DEC = StringHandle.str_join(oldChannel, partner);
                    collector.emit(Constant.USER_COUNT, input, new Values(null, DEC));
//
                    String OLD = StringHandle.str_join(oldChannel, an, Constant.AN_PASS, partner);
                    collector.emit(Constant.AN_RECORD, input, new Values(null, OLD));
                }
                break;
            }
            case "5": {
                // 表示收到心跳,有相同key且pt=0的记录则'续命'<10分钟>,否则不操作
                String macCopyV5 = jedis.get(macCopy);
                if (StringUtils.isNotBlank(macCopyV5)) {
                    jedis.expire(mac, Constant.TIME_TTL);
                } else {
                    Transaction transaction = jedis.multi();
                    transaction.set(mac, "0");
                    transaction.expire(mac, Constant.TIME_TTL);
                    transaction.set(macCopy, channelPartner);
                    List<Object> result = transaction.exec();
                    if (result == null || result.isEmpty()) {
                        log.warn("transaction is disturbed,and mac is {}", mac);
                        return;
                    }
                    String ADD = StringHandle.str_join(channelCode, partner);
                    collector.emit(Constant.USER_COUNT, input, new Values(ADD, null));
                    //
                    String NEW = StringHandle.str_join(channelCode, an, Constant.AN_PASS, partner);
                    collector.emit(Constant.AN_RECORD, input, new Values(NEW, null));
                }
                break;
            }
            default:
                break;
        }
    }

    public void cleanup() {
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declareStream(Constant.USER_COUNT, new Fields("ADD", "DEC"));
        declarer.declareStream(Constant.AN_RECORD, new Fields("NEW", "OLD"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

}

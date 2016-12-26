package cn.gitv.bi.rtliv.usercount.start;

import cn.gitv.bi.rtliv.usercount.bolts.AnRecord;
import cn.gitv.bi.rtliv.usercount.bolts.FilterAll;
import cn.gitv.bi.rtliv.usercount.bolts.Mac2Redis;
import cn.gitv.bi.rtliv.usercount.bolts.UserCount;
import cn.gitv.bi.rtliv.usercount.constant.Constant;
import cn.gitv.bi.rtliv.usercount.constant.Properties;
import kafka.api.OffsetRequest;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.kafka.*;
import org.apache.storm.redis.common.config.JedisPoolConfig;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import org.apache.storm.utils.Utils;

import static cn.gitv.bi.rtliv.usercount.constant.Properties.*;

public class Start_up {
    private static JedisPoolConfig jConf4Count = new JedisPoolConfig.Builder().setHost(REDIS_HOST_COMP).setPort(REDIS_PORT_1).setTimeout(4000).build();
    private static JedisPoolConfig jConf4An = new JedisPoolConfig.Builder().setHost(REDIS_HOST_COMP).setPort(REDIS_PORT_2).setTimeout(4000).build();

    /* 发生了119读取超时,而120没问题，说明119的数据量过载问题才是解决的根本*/
    private static SpoutConfig spoutBuild() {
        BrokerHosts brokerHosts = new ZkHosts(Properties.ZK_HOST_PORT);
        SpoutConfig spoutConfig = new SpoutConfig(brokerHosts, Properties.CONSUMER_TOPIC, "/storm", Properties.SPOUT_INZK);
        spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
        // 表示从kafka最新接手的数据开始
        spoutConfig.startOffsetTime = OffsetRequest.LatestTime();
        return spoutConfig;
    }

    public static void main(String[] args) {
        SpoutConfig spoutConfig = spoutBuild();
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("real_time_spout", new KafkaSpout(spoutConfig), 3);
        //bolt设置
        builder.setBolt("filter_bolt", new FilterAll(), 3).shuffleGrouping("real_time_spout");
        builder.setBolt("mac_redis_bolt", new Mac2Redis(), 4).fieldsGrouping("filter_bolt", new Fields("mac"));
        builder.setBolt("user_count_bolt", new UserCount(jConf4Count), 3).fieldsGrouping("mac_redis_bolt", Constant.USER_COUNT, new Fields("ADD", "DEC"));
        builder.setBolt("an_record_bolt", new AnRecord(jConf4An), 3).fieldsGrouping("mac_redis_bolt", Constant.AN_RECORD, new Fields("NEW", "OLD"));
        Config conf = new Config();
        conf.setMaxSpoutPending(2000);
        if (args == null || args.length == 0) {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("topo", conf, builder.createTopology());
            Utils.sleep(100000);
            cluster.killTopology("topo");
            cluster.shutdown();
        } else {
            conf.setNumWorkers(3);
            try {
                StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}

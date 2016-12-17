package cn.gitv.bi.rtvod.usercount.start;

import cn.gitv.bi.rtvod.usercount.bolts.FilterAll;
import cn.gitv.bi.rtvod.usercount.bolts.Mac2Redis;
import cn.gitv.bi.rtvod.usercount.bolts.UserCount;
import cn.gitv.bi.rtvod.usercount.constant.Properties;
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

public class Start_up {
    private static JedisPoolConfig jConf4Count = new JedisPoolConfig.Builder().setHost(Properties.REDIS_HOST_COMP).setPort(Properties.REDIS_PORT_1).setTimeout(4000).build();

    // 发生了119读取超时,而120没问题，说明119的数据量过载问题才是解决的根本
    private static SpoutConfig spoutBuild() {
        BrokerHosts brokerHosts = new ZkHosts(Properties.ZK_HOST_PORT);
        SpoutConfig spoutConfig = new SpoutConfig(brokerHosts, Properties.CONSUMER_TOPIC, "/storm", Properties.SPOUT_INZK);
        spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
        spoutConfig.startOffsetTime = OffsetRequest.LatestTime();// 表示从kafka最新接手的数据开始
        return spoutConfig;
    }

    public static void main(String[] args) {
        SpoutConfig spoutConfig = spoutBuild();
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("real_time_spout", new KafkaSpout(spoutConfig), 3);
        //
        builder.setBolt("filter_bolt", new FilterAll(), 3).shuffleGrouping("real_time_spout");
        builder.setBolt("mac_redis_bolt", new Mac2Redis(), 8).fieldsGrouping("filter_bolt", new Fields("mac"));
        builder.setBolt("user_count_bolt", new UserCount(jConf4Count), 3).fieldsGrouping("mac_redis_bolt", new Fields("ADD", "DEC"));
        Config conf = new Config();
        conf.setMaxSpoutPending(2000);
        if (args == null || args.length == 0) {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("topo", conf, builder.createTopology());
            Utils.sleep(100000);
            cluster.killTopology("topo");
            cluster.shutdown();
        } else {
            conf.setNumWorkers(4);
            try {
                StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

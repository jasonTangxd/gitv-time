package cn.gitv.bi.userinfo.appupdate.start;

import cn.gitv.bi.userinfo.appupdate.bolts.AppVersionIsChanged_Bolt;
import cn.gitv.bi.userinfo.appupdate.bolts.AppVersionUpdate_Bolt;
import cn.gitv.bi.userinfo.appupdate.bolts.TopFilter_Bolt;
import kafka.api.OffsetRequest;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.kafka.*;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.utils.Utils;

import static cn.gitv.bi.userinfo.appupdate.constant.Properties.*;

public class Start_up {
    private static SpoutConfig spoutConfBuild() {
        BrokerHosts brokerHosts = new ZkHosts(ZK_HOST_PORT);
        SpoutConfig spoutConfig = new SpoutConfig(brokerHosts, CONSUMER_TOPIC, "/storm", SPOUT_INZK);
        spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
        spoutConfig.startOffsetTime = OffsetRequest.LatestTime();// 表示从kafka最新接手的数据开始
        return spoutConfig;
    }

    public static void main(String[] args) {
        SpoutConfig spout_update = spoutConfBuild();
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("update_app_info", new KafkaSpout(spout_update), 3);
        builder.setBolt("top_filter", new TopFilter_Bolt(), 3).shuffleGrouping("update_app_info");
        builder.setBolt("version_is_changed", new AppVersionIsChanged_Bolt(), 3).shuffleGrouping("top_filter");
        builder.setBolt("update_app_version", new AppVersionUpdate_Bolt(), 3).shuffleGrouping("version_is_changed");
        Config conf = new Config();
        conf.setMaxSpoutPending(2000);
        conf.setMessageTimeoutSecs(60);
        if (args == null || args.length == 0) {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("topo", conf, builder.createTopology());
            Utils.sleep(100000);
            cluster.killTopology("topo");
            cluster.shutdown();
        } else {
            conf.setNumWorkers(1);
            try {
                StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

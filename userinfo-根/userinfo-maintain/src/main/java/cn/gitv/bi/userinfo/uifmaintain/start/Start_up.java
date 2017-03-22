package cn.gitv.bi.userinfo.uifmaintain.start;

import cn.gitv.bi.userinfo.uifmaintain.bolts.*;
import cn.gitv.bi.userinfo.uifmaintain.constant.LogConst;
import cn.gitv.bi.userinfo.uifmaintain.constant.Properties;
import kafka.api.OffsetRequest;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.kafka.*;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.utils.Utils;

public class Start_up {
    private static SpoutConfig spoutConfBuild() {
        BrokerHosts brokerHosts = new ZkHosts(Properties.ZK_HOST_PORT);
        SpoutConfig spoutConfig = new SpoutConfig(brokerHosts, Properties.CONSUMER_TOPIC, "/storm", Properties.SPOUT_INZK);
        spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
        spoutConfig.startOffsetTime = OffsetRequest.LatestTime();// 表示从kafka最新接手的数据开始
        return spoutConfig;
    }

    public static void main(String[] args) {
        SpoutConfig spoutConfig = spoutConfBuild();
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("maintain_userinfo", new KafkaSpout(spoutConfig), 3);
        builder.setBolt("topfilter", new TopFilter_Bolt(), 3).shuffleGrouping("maintain_userinfo");
        builder.setBolt("distinguish", new Distinguish_Bolt(), 3).shuffleGrouping("topfilter");

        builder.setBolt("no_provincetop", new NoProvinceToP_Bolt(), 1).shuffleGrouping("distinguish", LogConst.NOPTP);
        builder.setBolt("rabbit_mq", new RabbitMQ_Bolt(), 3).shuffleGrouping("distinguish", LogConst.RAB);
        builder.setBolt("update_uinfo", new UpdateUinfo_Bolt(), 3).shuffleGrouping("distinguish", LogConst.UPD);
        //fopen
        builder.setBolt("fopen_isnull", new FOpenIsNull_Bolt(), 6).shuffleGrouping("update_uinfo", LogConst.FOPEN);
        builder.setBolt("fopen_update", new FOpen_Update_Bolt(), 6).shuffleGrouping("fopen_isnull");
        //appV
        builder.setBolt("appv_isnull", new AppVersionIsNull_Bolt(), 6).shuffleGrouping("update_uinfo", LogConst.APPV);
        builder.setBolt("appv_update", new AppVersion_Update_Bolt(), 6).shuffleGrouping("appv_isnull");
        //nojudge
        builder.setBolt("nojudge_update", new NoJudgeUpdate_Bolt(), 6).shuffleGrouping("update_uinfo", LogConst.NOJUDGE);
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
            conf.setNumWorkers(4);
            try {
                StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

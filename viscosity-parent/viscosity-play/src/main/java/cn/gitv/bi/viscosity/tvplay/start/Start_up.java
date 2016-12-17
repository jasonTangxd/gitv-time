package cn.gitv.bi.viscosity.tvplay.start;
import cn.gitv.bi.viscosity.tvplay.bolts.*;
import cn.gitv.bi.viscosity.tvplay.constant.Constant;
import cn.gitv.bi.viscosity.tvplay.constant.Properties;
import kafka.api.OffsetRequest;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.kafka.*;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.utils.Utils;

/**
 * Hello world!
 */
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
        builder.setSpout("viscosity_spout", new KafkaSpout(spoutConfig), 3);
        builder.setBolt("top_filter", new TopFilter_Bolt(), 3).shuffleGrouping("viscosity_spout");
        //
        builder.setBolt("vod_distribute", new VOD_Distribute_Bolt(), 3).shuffleGrouping("top_filter", Constant.VOD_DIS);
        builder.setBolt("nosrcName", new NoSrcName_Bolt(), 1).shuffleGrouping("vod_distribute", Constant.NOSRCNAME).shuffleGrouping("liv_distribute", Constant.NOSRCNAME);
        builder.setBolt("vod_user_viscosity", new VOD_UserViscosity_Bolt(), 3).shuffleGrouping("vod_distribute", Constant.VOD_TO_USER);
        builder.setBolt("vod_to_user_insert", new VOD_ToUser_Insert_Bolt(), 3).shuffleGrouping("vod_user_viscosity", Constant.VOD_TO_USER_INSERT);
        builder.setBolt("vod_to_user_update", new VOD_ToUser_Update_Bolt(), 3).shuffleGrouping("vod_user_viscosity", Constant.VOD_TO_USER_UPDATE);
        builder.setBolt("vod_program_viscosity", new VOD_ProgramViscosity_Bolt(), 3).shuffleGrouping("vod_distribute", Constant.VOD_TO_PROGRAM);
        builder.setBolt("vod_to_program_insert", new VOD_ToProgram_Insert_Bolt(), 3).shuffleGrouping("vod_program_viscosity", Constant.VOD_TO_PROGRAM_INSERT);
        builder.setBolt("vod_to_program_update", new VOD_ToProgram_Update_Bolt(), 3).shuffleGrouping("vod_program_viscosity", Constant.VOD_TO_PROGRAM_UPDATE);
        //
        builder.setBolt("liv_distribute", new LIV_Distribute_Bolt(), 3).shuffleGrouping("top_filter", Constant.LIV_DIS);
        builder.setBolt("liv_user_viscosity", new LIV_UserViscosity_Bolt(), 3).shuffleGrouping("liv_distribute", Constant.LIV_TO_USER);
        builder.setBolt("liv_to_user_insert", new LIV_ToUser_Insert_Bolt(), 3).shuffleGrouping("liv_user_viscosity", Constant.LIV_TO_USER_INSERT);
        builder.setBolt("liv_to_user_update", new LIV_ToUser_Update_Bolt(), 3).shuffleGrouping("liv_user_viscosity", Constant.LIV_TO_USER_UPDATE);
        builder.setBolt("liv_program_viscosity", new LIV_ProgramViscosity_Bolt(), 3).shuffleGrouping("liv_distribute", Constant.LIV_TO_PROGRAM);
        builder.setBolt("liv_to_program_insert", new LIV_ToProgram_Insert_Bolt(), 3).shuffleGrouping("liv_program_viscosity", Constant.LIV_TO_PROGRAM_INSERT);
        builder.setBolt("liv_to_program_update", new LIV_ToProgram_Update_Bolt(), 3).shuffleGrouping("liv_program_viscosity", Constant.LIV_TO_PROGRAM_UPDATE);
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
            conf.setNumWorkers(6);
            try {
                StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}

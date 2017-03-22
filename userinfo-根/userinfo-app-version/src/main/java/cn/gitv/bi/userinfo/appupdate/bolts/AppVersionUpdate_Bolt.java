package cn.gitv.bi.userinfo.appupdate.bolts;
import cn.gitv.bi.userinfo.appupdate.constant.Constant;
import cn.gitv.bi.userinfo.appupdate.storage.AsyncBack;
import cn.gitv.bi.userinfo.appupdate.storage.Mapper;
import cn.gitv.bi.userinfo.appupdate.utils.CassandraConnection;
import com.datastax.driver.core.Session;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class AppVersionUpdate_Bolt implements IRichBolt {

    private static final long serialVersionUID = 1688757578656330072L;
    /**
     *
     */
    private OutputCollector collector = null;
    private Logger log = LoggerFactory.getLogger(AppVersionUpdate_Bolt.class);
    private final static String UPDATE_USERINFO_Map1 = "UPDATE user_info SET livod_app_version='%s', livod_update_history = livod_update_history + { '%s' : '%s'} WHERE partner='%s' and mac_addr='%s';";
    private final static String UPDATE_USERINFO_Map2 = "UPDATE user_info SET vod_app_version='%s', vod_update_history = vod_update_history + { '%s' : '%s'} WHERE partner='%s' and mac_addr='%s';";
    private final static String UPDATE_USERINFO_Map3 = "UPDATE user_info SET cnm_app_version='%s', cnm_update_history = cnm_update_history + { '%s' : '%s'} WHERE partner='%s' and mac_addr='%s';";
    private Session session;

    @SuppressWarnings("rawtypes")
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        session = CassandraConnection.getSession();
    }

    public void execute(Tuple input) {
        try {
            String type = input.getStringByField("enumtype");
            String partner = input.getStringByField("partner");
            String mac = input.getStringByField("mac");
            String app_version = input.getStringByField("app_version");
            String logtime = input.getStringByField("logtime");
            String cql_excute = null;
            switch (type) {
                case Constant.LIVOD:
                    cql_excute = Mapper.update_uif_map_cql(UPDATE_USERINFO_Map1, app_version, logtime, partner, mac);
                    break;
                case Constant.V0_VOD:
                case Constant.V1_VOD:
                    cql_excute = Mapper.update_uif_map_cql(UPDATE_USERINFO_Map2, app_version, logtime, partner, mac);
                    break;
                case Constant.CNM:
                    cql_excute = Mapper.update_uif_map_cql(UPDATE_USERINFO_Map3, app_version, logtime, partner, mac);
                    break;
                default:
                    return;
            }
            AsyncBack.update_back(session, cql_excute, collector, input);
        } catch (Exception e) {
            log.error("AppUpdate_Bolt split exception", e.getMessage());
            collector.fail(input);
        }

    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }

    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

    public void cleanup() {
    }
}

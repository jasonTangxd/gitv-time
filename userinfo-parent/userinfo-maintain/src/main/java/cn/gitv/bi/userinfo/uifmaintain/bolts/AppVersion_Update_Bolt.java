package cn.gitv.bi.userinfo.uifmaintain.bolts;

import cn.gitv.bi.userinfo.uifmaintain.constant.LogConst;
import cn.gitv.bi.userinfo.uifmaintain.storage.AsyncBack;
import cn.gitv.bi.userinfo.uifmaintain.storage.Mapper;
import cn.gitv.bi.userinfo.uifmaintain.utils.CassandraConnection;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class AppVersion_Update_Bolt implements IRichBolt {
    /**
     *
     */
    private Logger log = LoggerFactory.getLogger(AppVersion_Update_Bolt.class);
    private static final long serialVersionUID = -2838932529064300841L;
    private OutputCollector collector = null;
    private static final String UPDATE_APP1 = "update user_info set livod_app_version=? where partner =? and mac_addr=?;";
    private static final String UPDATE_APP2 = "update user_info set vod_app_version=? where partner =? and mac_addr=?;";
    private static final String UPDATE_APP3 = "update user_info set cnm_app_version=? where partner =? and mac_addr=?;";
    private PreparedStatement ps1 = null;
    private PreparedStatement ps2 = null;
    private PreparedStatement ps3 = null;
    private Session session = null;

    @SuppressWarnings("rawtypes")
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        this.session = CassandraConnection.getSession();
        this.ps1 = session.prepare(UPDATE_APP1);
        this.ps2 = session.prepare(UPDATE_APP2);
        this.ps3 = session.prepare(UPDATE_APP3);
    }

    public void execute(Tuple input) {
        try {
            String type = input.getStringByField("enumtype");
            String mac = input.getStringByField("mac");
            String partner = input.getStringByField("partner");
            String app_version = input.getStringByField("app_version");
            switch (type) {
                case LogConst.LIVOD:
                    BoundStatement bs1 = Mapper.appv_update(ps1, partner, mac, app_version);
                    AsyncBack.update_back(session, bs1, collector, input);
                    break;
                case LogConst.V0_VOD:
                case LogConst.V1_VOD:
                    BoundStatement bs2 = Mapper.appv_update(ps2, partner, mac, app_version);
                    AsyncBack.update_back(session, bs2, collector, input);
                    break;
                case LogConst.CNM:
                    BoundStatement bs3 = Mapper.appv_update(ps3, partner, mac, app_version);
                    AsyncBack.update_back(session, bs3, collector, input);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            log.error("", e);
            collector.fail(input);
        }
    }

    public void cleanup() {
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }

    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

}

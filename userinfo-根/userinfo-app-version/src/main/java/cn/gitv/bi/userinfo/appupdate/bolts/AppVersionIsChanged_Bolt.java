package cn.gitv.bi.userinfo.appupdate.bolts;

import cn.gitv.bi.userinfo.appupdate.constant.Constant;
import cn.gitv.bi.userinfo.appupdate.storage.AsyncBack;
import cn.gitv.bi.userinfo.appupdate.storage.Mapper;
import cn.gitv.bi.userinfo.appupdate.utils.CassandraConnection;
import cn.gitv.bi.userinfo.appupdate.utils.StringHandle;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class AppVersionIsChanged_Bolt implements IRichBolt {

    private static final long serialVersionUID = 1688757578656330072L;
    private OutputCollector collector = null;
    private Logger log = LoggerFactory.getLogger(AppVersionIsChanged_Bolt.class);
    private static final String GETAPPVERSION1 = "SELECT livod_app_version FROM user_info WHERE partner=? and mac_addr=?;";
    private static final String GETAPPVERSION2 = "SELECT vod_app_version FROM user_info WHERE partner=? and mac_addr=?;";
    private static final String GETAPPVERSION3 = "SELECT cnm_app_version FROM user_info WHERE partner=? and mac_addr=?;";
    private Session session;
    private PreparedStatement ps1;
    private PreparedStatement ps2;
    private PreparedStatement ps3;

    @SuppressWarnings("rawtypes")
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        session = CassandraConnection.getSession();
        ps1 = session.prepare(GETAPPVERSION1);
        ps2 = session.prepare(GETAPPVERSION2);
        ps3 = session.prepare(GETAPPVERSION3);
    }

    public void execute(Tuple input) {
        try {
            String type = input.getStringByField("enumtype");
            String partner = input.getStringByField("partner");
            String mac = input.getStringByField("mac");
            String app_version = input.getStringByField("app_version");
            String update_res = input.getStringByField("update_res");
            String logtime = input.getStringByField("logtime");
//            log.debug("Topfilter BaseLogTransform is enumtype:{}, partner:{}, mac:{} ,app_version:{} ,update_res:{},{}", enumtype, partner, mac, app_version, update_res, logtime);
            if (!StringHandle.isLegalField(type,partner, mac, app_version, update_res, logtime)) {
                collector.ack(input);
                return;
            }
            if ("0".equals(update_res)) {
                // 为0表示升级成功
                switch (type) {
                    case Constant.LIVOD:
                        BoundStatement read1 = Mapper.read_appVersion(ps1, partner, mac);
                        AsyncBack.appVersion_read_async(session, read1, collector, input);
                        break;
                    case Constant.V0_VOD:
                    case Constant.V1_VOD:
                        BoundStatement read2 = Mapper.read_appVersion(ps2, partner, mac);
                        AsyncBack.appVersion_read_async(session, read2, collector, input);
                        break;
                    case Constant.CNM:
                        BoundStatement read3 = Mapper.read_appVersion(ps3, partner, mac);
                        AsyncBack.appVersion_read_async(session, read3, collector, input);
                        break;
                    default:
                        break;
                }
            } else {
                log.debug("the update_res is--{},is not 0",update_res);
                collector.ack(input);
            }
        } catch (Exception e) {
            log.error("AppUpdate_Bolt split exception{}", e.getMessage());
            collector.fail(input);
        }

    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("enumtype", "partner", "mac", "app_version", "logtime"));
    }

    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

    public void cleanup() {
    }
}

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
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class AppVersionIsNull_Bolt implements IRichBolt {
    /**
     *
     */
    private Logger log = LoggerFactory.getLogger(AppVersionIsNull_Bolt.class);
    private static final long serialVersionUID = -2838932529064300841L;
    private OutputCollector collector = null;
    private static final String APPISNULL1 = "select count(livod_app_version) from user_info where partner=? and mac_addr=?";
    private static final String APPISNULL2 = "select count(vod_app_version) from user_info where partner=? and mac_addr=?";
    private static final String APPISNULL3 = "select count(cnm_app_version) from user_info where partner=? and mac_addr=?";
    private PreparedStatement ps1 = null;
    private PreparedStatement ps2 = null;
    private PreparedStatement ps3 = null;
    private Session session = null;

    @SuppressWarnings("rawtypes")
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        this.session = CassandraConnection.getSession();
        this.ps1 = session.prepare(APPISNULL1);
        this.ps2 = session.prepare(APPISNULL2);
        this.ps3 = session.prepare(APPISNULL3);
    }

    public void execute(Tuple input) {
        try {
            String type = input.getStringByField("enumtype");
            String mac = input.getStringByField("mac");
            String partner = input.getStringByField("partner");
//            String app_version = input.getStringByField("app_version");
            switch (type) {
                case LogConst.LIVOD:
                    BoundStatement read1 = Mapper.fopen_isnull(ps1, partner, mac);
                    AsyncBack.appversion_isnull(session, read1, collector, input);
                    break;
                case LogConst.V0_VOD:
                case LogConst.V1_VOD:
                    BoundStatement read2 = Mapper.fopen_isnull(ps2, partner, mac);
                    AsyncBack.appversion_isnull(session, read2, collector, input);
                    break;
                case LogConst.CNM:
                    BoundStatement read3 = Mapper.fopen_isnull(ps3, partner, mac);
                    AsyncBack.appversion_isnull(session, read3, collector, input);
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
        declarer.declare(new Fields("enumtype", "mac", "partner", "app_version"));
    }

    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

}

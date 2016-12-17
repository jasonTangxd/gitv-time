package cn.gitv.bi.viscosity.tvplay.bolts;
import cn.gitv.bi.viscosity.tvplay.constant.Constant;
import cn.gitv.bi.viscosity.tvplay.storage.AsyncBack_Liv;
import cn.gitv.bi.viscosity.tvplay.storage.Mapper_Liv;
import cn.gitv.bi.viscosity.tvplay.utils.CassandraConnection;
import cn.gitv.bi.viscosity.tvplay.utils.OnlyDateUtils;
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

import java.util.Date;
import java.util.Map;

public class LIV_UserViscosity_Bolt implements IRichBolt {
    /**
     *
     */
    private OutputCollector collector = null;
    private static final long serialVersionUID = -5516726617765479099L;
    private Logger log = LoggerFactory.getLogger(LIV_UserViscosity_Bolt.class);
    private final static String read_user = "select count(*) from liv_viscosity_user where partner= ? and mac= ? and srcName= ? and chnCode= ? and albumName= ? and playOrder= ? and logDate= ?;";
    private Session session = null;
    private PreparedStatement ps = null;

    @SuppressWarnings("rawtypes")
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        this.session = CassandraConnection.getSession();
        ps = session.prepare(read_user);
    }

    public void execute(Tuple input) {
        try {
            String partner = input.getStringByField("partner");
            String mac = input.getStringByField("mac");
            String srcName = input.getStringByField("srcName");
            String chnCode = input.getStringByField("chnCode");
            String albumName = input.getStringByField("albumName");
            String playOrder = input.getStringByField("playOrder");
            String logDate = input.getStringByField("logDate");
            Date lg_date = OnlyDateUtils.parseTimestamp(logDate);
            BoundStatement read = Mapper_Liv.read_user(ps, partner, mac, srcName, chnCode, albumName, playOrder, lg_date);
            AsyncBack_Liv.user_read_async(session, read, collector, input);
        } catch (Exception e) {
            log.error("LIV_UserViscosity_Bolt exception:" + e.getMessage());
            collector.fail(input);
        }
    }

    public void cleanup() {
        // TODO Auto-generated method stub

    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declareStream(Constant.LIV_TO_USER_INSERT, new Fields("partner", "mac", "srcName", "chnCode", "albumName",
                "playOrder", "playLength", "timeLength", "logDate"));
        declarer.declareStream(Constant.LIV_TO_USER_UPDATE,
                new Fields("partner", "mac", "srcName", "chnCode", "albumName", "playOrder", "playLength", "logDate"));
    }

    public Map<String, Object> getComponentConfiguration() {
        // TODO Auto-generated method stub
        return null;
    }

}

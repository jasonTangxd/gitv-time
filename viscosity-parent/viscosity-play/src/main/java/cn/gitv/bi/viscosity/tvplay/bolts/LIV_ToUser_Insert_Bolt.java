package cn.gitv.bi.viscosity.tvplay.bolts;
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
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;

public class LIV_ToUser_Insert_Bolt implements IRichBolt {
    /**
     *
     */
    private OutputCollector collector = null;
    private static final long serialVersionUID = -5516726617765479099L;
    private Logger log = LoggerFactory.getLogger(LIV_ToUser_Insert_Bolt.class);
    private final static String insert_new_user = "update liv_viscosity_user set click_num = click_num + 1,playLength = playLength + ?,timeLength = timeLength + ? where partner= ? and mac= ? and srcName= ? and chnCode= ? and albumName= ? and playOrder= ? and logDate= ?;";
    private Session session = null;
    private PreparedStatement ps = null;

    @SuppressWarnings("rawtypes")
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        this.session = CassandraConnection.getSession();
        ps = session.prepare(insert_new_user);
    }

    public void execute(Tuple input) {
        try {
            String partner = input.getStringByField("partner");
            String mac = input.getStringByField("mac");
            String srcName = input.getStringByField("srcName");
            String chnCode = input.getStringByField("chnCode");
            String albumName = input.getStringByField("albumName");
            String playOrder = input.getStringByField("playOrder");
            long playLength = input.getLongByField("playLength");
            long timeLength = input.getLongByField("timeLength");
            String logDate = input.getStringByField("logDate");
            Date lg_date = OnlyDateUtils.parseTimestamp(logDate);
            BoundStatement bs = Mapper_Liv.insert_new_user(ps, partner, mac, srcName, chnCode, albumName, playOrder, playLength, timeLength, lg_date);
            AsyncBack_Liv.update_back(session, bs, collector, input);
        } catch (Exception e) {
            log.debug("LIV_ToUser_Insert_Bolt exception:" + e.getMessage());
            collector.fail(input);
        }
    }

    public void cleanup() {
        // TODO Auto-generated method stub

    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // TODO Auto-generated method stub

    }

    public Map<String, Object> getComponentConfiguration() {
        // TODO Auto-generated method stub
        return null;
    }

}

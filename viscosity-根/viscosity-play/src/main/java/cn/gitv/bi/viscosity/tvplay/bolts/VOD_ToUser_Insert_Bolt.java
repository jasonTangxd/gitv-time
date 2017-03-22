package cn.gitv.bi.viscosity.tvplay.bolts;
import cn.gitv.bi.viscosity.tvplay.storage.AsyncBack;
import cn.gitv.bi.viscosity.tvplay.storage.Mapper;
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

public class VOD_ToUser_Insert_Bolt implements IRichBolt {
    /**
     *
     */
    private OutputCollector collector = null;
    private static final long serialVersionUID = -5516726617765479099L;
    private Logger log = LoggerFactory.getLogger(VOD_ToUser_Insert_Bolt.class);
    private final static String insert_new_user = "update vod_viscosity_user set click_num = click_num + 1,playLength = playLength + ?,timeLength = timeLength + ? where partner= ? and mac= ? and srcName= ? and chnName= ? and chnId= ? and albumName= ? and albumId= ? and playOrder= ? and videoId= ? and logDate= ?;";
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
            String chnName = input.getStringByField("chnName");
            String chnId = input.getStringByField("chnId");
            String albumName = input.getStringByField("albumName");
            String albumId = input.getStringByField("albumId");
            String playOrder = input.getStringByField("playOrder");
            String videoId = input.getStringByField("videoId");
            long playLength = input.getLongByField("playLength");
            long timeLength = input.getLongByField("timeLength");
            String logDate = input.getStringByField("logDate");
            Date lg_date = OnlyDateUtils.parseTimestamp(logDate);
            BoundStatement bs = Mapper.insert_new_user(ps, partner, mac, srcName, chnName, chnId, albumName, albumId, playOrder, videoId,
                    playLength, timeLength, lg_date);
            AsyncBack.update_back(session, bs, collector, input);
        } catch (Exception e) {
            log.debug("VOD_ToUser_Insert_Bolt exception:" + e.getMessage());
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

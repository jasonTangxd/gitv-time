package cn.gitv.bi.viscosity.tvplay.bolts;
import cn.gitv.bi.viscosity.tvplay.constant.Constant;
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
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;

public class VOD_UserViscosity_Bolt implements IRichBolt {
    /**
     *
     */
    private OutputCollector collector = null;
    private static final long serialVersionUID = -5516726617765479099L;
    private Logger log = LoggerFactory.getLogger(VOD_UserViscosity_Bolt.class);
    private final static String read_user = "select count(*) from vod_viscosity_user where partner= ? and mac= ? and srcName= ? and chnName= ? and chnId= ? and albumName= ? and albumId= ? and playOrder= ? and videoId= ? and logDate= ?;";
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
            String chnName = input.getStringByField("chnName");
            String chnId = input.getStringByField("chnId");
            String albumName = input.getStringByField("albumName");
            String albumId = input.getStringByField("albumId");
            String playOrder = input.getStringByField("playOrder");
            String videoId = input.getStringByField("videoId");
            String logDate = input.getStringByField("logDate");
            Date lg_date = OnlyDateUtils.parseTimestamp(logDate);
            BoundStatement read = Mapper.read_user(ps, partner, mac, srcName, chnName, chnId, albumName, albumId, playOrder, videoId, lg_date);
            AsyncBack.user_read_async(session, read, collector, input);
        } catch (Exception e) {
            log.error("VOD_UserViscosity_Bolt exception:" + e.getMessage());
            collector.fail(input);
        }
    }

    public void cleanup() {
        // TODO Auto-generated method stub

    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declareStream(Constant.VOD_TO_USER_INSERT, new Fields("partner", "mac", "srcName", "chnName", "chnId", "albumName", "albumId",
                "playOrder", "videoId", "playLength", "timeLength", "logDate"));
        declarer.declareStream(Constant.VOD_TO_USER_UPDATE,
                new Fields("partner", "mac", "srcName", "chnName", "chnId", "albumName", "albumId", "playOrder", "videoId", "playLength", "logDate"));
    }

    public Map<String, Object> getComponentConfiguration() {
        // TODO Auto-generated method stub
        return null;
    }

}

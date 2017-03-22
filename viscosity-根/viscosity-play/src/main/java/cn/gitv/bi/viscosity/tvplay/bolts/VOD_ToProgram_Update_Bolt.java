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

public class VOD_ToProgram_Update_Bolt implements IRichBolt {

    /**
     *
     */
    private static final long serialVersionUID = -8321838025849388725L;
    private OutputCollector collector = null;
    private Logger log = LoggerFactory.getLogger(VOD_ToProgram_Update_Bolt.class);
    private final static String update_new_program = "update vod_viscosity_program set click_num = click_num + 1,playLength = playLength + ? where partner= ? and srcName= ? and chnName= ? and chnId= ? and albumName= ? and albumId= ? and playOrder= ? and videoId= ? and province= ? and city= ? and logDate= ?;";
    private Session session = null;
    private PreparedStatement ps = null;

    @SuppressWarnings("rawtypes")
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        this.session = CassandraConnection.getSession();
        ps = session.prepare(update_new_program);
    }

    public void execute(Tuple input) {
        try {
            String partner = input.getStringByField("partner");
            String srcName = input.getStringByField("srcName");
            String chnName = input.getStringByField("chnName");
            String chnId = input.getStringByField("chnId");
            String albumName = input.getStringByField("albumName");
            String albumId = input.getStringByField("albumId");
            String playOrder = input.getStringByField("playOrder");
            String videoId = input.getStringByField("videoId");
            String city = input.getStringByField("city");
            String province = input.getStringByField("province");
            long playLength = input.getLongByField("playLength");
            String logDate = input.getStringByField("logDate");
            Date lg_date = OnlyDateUtils.parseTimestamp(logDate);
            BoundStatement bs = Mapper.update_new_program(ps, partner, srcName, chnName, chnId, albumName, albumId, playOrder, videoId, province,
                    city, playLength, lg_date);
            AsyncBack.update_back(session, bs, collector, input);
        } catch (Exception e) {
            log.error("VOD_ToProgram_Update_Bolt exception:" + e.getMessage());
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

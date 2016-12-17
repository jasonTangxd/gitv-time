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

public class LIV_ProgramViscosity_Bolt implements IRichBolt {

    /**
     *
     */
    private static final long serialVersionUID = -8321838025849388725L;
    private OutputCollector collector = null;
    private Logger log = LoggerFactory.getLogger(LIV_ProgramViscosity_Bolt.class);
    private final static String read_program = "select count(*) from liv_viscosity_program where partner= ? and srcName= ? and chnCode= ? and albumName= ?  and playOrder= ?  and province= ? and city= ? and logDate= ?;";
    private Session session = null;
    private PreparedStatement ps = null;

    @SuppressWarnings("rawtypes")
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        this.session = CassandraConnection.getSession();
        ps = session.prepare(read_program);
    }

    public void execute(Tuple input) {
        try {
            String partner = input.getStringByField("partner");
            String srcName = input.getStringByField("srcName");
            String chnCode = input.getStringByField("chnCode");
            String albumName = input.getStringByField("albumName");
            String playOrder = input.getStringByField("playOrder");
            String province = input.getStringByField("province");
            String city = input.getStringByField("city");
            String logDate = input.getStringByField("logDate");
            Date lg_date = OnlyDateUtils.parseTimestamp(logDate);
            BoundStatement read = Mapper_Liv.read_program(ps, partner, srcName, chnCode, albumName, playOrder, province, city, lg_date);
            AsyncBack_Liv.program_read_async(session, read, collector, input);
        } catch (Exception e) {
            log.error("LIV_ProgramViscosity_Bolt exception:--{}", e.getMessage());
            collector.fail(input);
        }
    }

    public void cleanup() {
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declareStream(Constant.LIV_TO_PROGRAM_INSERT, new Fields("partner", "srcName", "chnCode", "albumName",
                "playOrder", "province", "city", "playLength", "timeLength", "logDate"));
        declarer.declareStream(Constant.LIV_TO_PROGRAM_UPDATE, new Fields("partner", "srcName", "chnCode", "albumName",
                "playOrder", "province", "city", "playLength", "logDate"));
    }

    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

}

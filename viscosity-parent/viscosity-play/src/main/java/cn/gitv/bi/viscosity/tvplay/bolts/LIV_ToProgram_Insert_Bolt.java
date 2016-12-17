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

public class LIV_ToProgram_Insert_Bolt implements IRichBolt {

    /**
     *
     */
    private static final long serialVersionUID = -8321838025849388725L;
    private OutputCollector collector = null;
    private Logger log = LoggerFactory.getLogger(LIV_ToProgram_Insert_Bolt.class);
    private final static String insert_new_program = "update liv_viscosity_program set click_num = click_num + 1,playLength = playLength + ?,timeLength = timeLength + ? where partner= ? and srcName= ? and chnCode= ? and albumName= ? and playOrder= ? and province= ? and city= ? and logDate= ?;";
    private Session session = null;
    private PreparedStatement ps = null;

    @SuppressWarnings("rawtypes")
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        this.session = CassandraConnection.getSession();
        ps = session.prepare(insert_new_program);
    }

    public void execute(Tuple input) {
        try {
            String partner = input.getStringByField("partner");
            String srcName = input.getStringByField("srcName");
            String chnCode = input.getStringByField("chnCode");
            String albumName = input.getStringByField("albumName");
            String playOrder = input.getStringByField("playOrder");
            String city = input.getStringByField("city");
            String province = input.getStringByField("province");
            long playLength = input.getLongByField("playLength");
            long timeLength = input.getLongByField("timeLength");
            String logDate = input.getStringByField("logDate");
            Date lg_date = OnlyDateUtils.parseTimestamp(logDate);
            BoundStatement bs = Mapper_Liv.insert_new_program(ps, partner, srcName, chnCode, albumName, playOrder, province, city, playLength, timeLength, lg_date);
            AsyncBack_Liv.update_back(session, bs, collector, input);
        } catch (Exception e) {
            log.error("LIV_ToProgram_Insert_Bolt exception:{}", e.getMessage());
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

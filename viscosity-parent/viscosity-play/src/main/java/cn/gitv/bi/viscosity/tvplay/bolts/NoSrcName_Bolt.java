package cn.gitv.bi.viscosity.tvplay.bolts;
import cn.gitv.bi.viscosity.tvplay.storage.AsyncBack_Liv;
import cn.gitv.bi.viscosity.tvplay.storage.Mapper;
import cn.gitv.bi.viscosity.tvplay.utils.CassandraConnection;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;

import java.util.Map;

public class NoSrcName_Bolt implements IRichBolt {
    private static final long serialVersionUID = 2040513490773052967L;
    /**
     *
     */
    private OutputCollector collector = null;
    private Session session = null;
    private PreparedStatement ps = null;
    private static final String nosrcid_mean = "insert into viscosity_user_nosrcid_mean(srcid) values (?)";

    @SuppressWarnings("rawtypes")
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        this.session = CassandraConnection.getSession();
        ps = session.prepare(nosrcid_mean);
    }

    public void execute(Tuple input) {
        try {
            String srcId = input.getStringByField("srcId");
            BoundStatement bs = Mapper.pre_nosrcid_mean(ps, srcId);
            AsyncBack_Liv.update_back(session, bs, collector, input);
        } catch (Exception e) {
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

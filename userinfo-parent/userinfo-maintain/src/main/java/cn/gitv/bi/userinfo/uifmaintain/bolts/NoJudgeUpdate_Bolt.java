package cn.gitv.bi.userinfo.uifmaintain.bolts;

import cn.gitv.bi.userinfo.uifmaintain.storage.AsyncBack;
import cn.gitv.bi.userinfo.uifmaintain.storage.Mapper;
import cn.gitv.bi.userinfo.uifmaintain.utils.CassandraConnection;
import cn.gitv.bi.userinfo.uifmaintain.utils.SimpleDateUtils;
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

public class NoJudgeUpdate_Bolt implements IRichBolt {
    /**
     *
     */
    private Logger log = LoggerFactory.getLogger(NoJudgeUpdate_Bolt.class);
    private static final long serialVersionUID = -2838932529064300841L;
    private OutputCollector collector = null;
    private static final String UPDATE_NoJudge = "update user_info set province=?,last_open=? where partner =? and mac_addr=?;";
    private PreparedStatement ps = null;
    private Session session = null;

    @SuppressWarnings("rawtypes")
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        this.session = CassandraConnection.getSession();
        this.ps = session.prepare(UPDATE_NoJudge);
    }

    public void execute(Tuple input) {
        try {
            String mac = input.getStringByField("mac");
            String partner = input.getStringByField("partner");
            String last_open = input.getStringByField("last_open");
            String province = input.getStringByField("province");
            Date lastopen = SimpleDateUtils.parseTimestamp(last_open);
            BoundStatement bs = Mapper.nojudge_update(ps, partner, mac, lastopen, province);
            AsyncBack.update_back(session, bs, collector, input);
        } catch (Exception e) {
            log.error("", e);
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

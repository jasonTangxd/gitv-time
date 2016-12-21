package cn.gitv.bi.userinfo.uifmaintain.bolts;

import java.util.Map;

import cn.gitv.bi.userinfo.uifmaintain.storage.AsyncBack;
import cn.gitv.bi.userinfo.uifmaintain.storage.Mapper;
import cn.gitv.bi.userinfo.uifmaintain.utils.CassandraConnection;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;

public class FOpenIsNull_Bolt implements IRichBolt {
	/**
	 * 
	 */
	private Logger log = LoggerFactory.getLogger(FOpenIsNull_Bolt.class);
	private static final long serialVersionUID = -2838932529064300841L;
	private OutputCollector collector = null;
	private static final String FOPENISNULL = "select count(first_open) from user_info where partner=? and mac_addr=?;";
	private PreparedStatement ps = null;
	private Session session = null;

	@SuppressWarnings("rawtypes")
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		this.session = CassandraConnection.getSession();
		this.ps = session.prepare(FOPENISNULL);
	}

	public void execute(Tuple input) {
		try {
			String mac = input.getStringByField("mac");
			String partner = input.getStringByField("partner");
			BoundStatement read = Mapper.fopen_isnull(ps, partner, mac);
			AsyncBack.fopen_isnull(session, read, collector, input);
		} catch (Exception e) {
			log.error("", e);
			collector.fail(input);
		}
	}

	public void cleanup() {

	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("mac", "partner", "last_open"));
	}

	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

}

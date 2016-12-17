package cn.gitv.bi.userinfo.uifmaintain.bolts;

import java.util.Map;

import cn.gitv.bi.userinfo.uifmaintain.storage.AsyncBack;
import cn.gitv.bi.userinfo.uifmaintain.storage.Mapper;
import cn.gitv.bi.userinfo.uifmaintain.utils.CassandraConnection;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;

public class NoProvinceToP_Bolt implements IRichBolt {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5000321397978574674L;
	private OutputCollector collector = null;
	private Logger log = LoggerFactory.getLogger(NoProvinceToP_Bolt.class);
	private Session session = null;
	private static final String PTOP = "insert into nopartner_province (partner) values (?);";
	private PreparedStatement ps = null;

	@SuppressWarnings("rawtypes")
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		this.session = CassandraConnection.getSession();
		ps = session.prepare(PTOP);
	}

	public void execute(Tuple input) {
		try {
			String partner = input.getStringByField("partner");
			BoundStatement bs= Mapper.noPToProvince(ps, partner);
			AsyncBack.update_back(session, bs, collector, input);
		} catch (Exception e) {
			log.error("NoProvinceToP_Bolt exception{}",e.getMessage());
			collector.fail(input);
		}

	}

	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	public void cleanup() {
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub

	}

}

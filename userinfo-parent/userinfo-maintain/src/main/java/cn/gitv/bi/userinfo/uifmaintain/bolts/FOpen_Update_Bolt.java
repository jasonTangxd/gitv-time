package cn.gitv.bi.userinfo.uifmaintain.bolts;

import java.util.Date;
import java.util.Map;

import cn.gitv.bi.userinfo.uifmaintain.storage.AsyncBack;
import cn.gitv.bi.userinfo.uifmaintain.storage.Mapper;
import cn.gitv.bi.userinfo.uifmaintain.utils.CassandraConnection;
import cn.gitv.bi.userinfo.uifmaintain.utils.OnlyDateUtils;
import cn.gitv.bi.userinfo.uifmaintain.utils.SimpleDateUtils;
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
public class FOpen_Update_Bolt implements IRichBolt {
	/**
	 * 
	 */
	private Logger log = LoggerFactory.getLogger(FOpen_Update_Bolt.class);
	private static final long serialVersionUID = -2838932529064300841L;
	private OutputCollector collector = null;
	private PreparedStatement ps = null;
	private Session session = null;
	private static final String UPDATE_FO_RD = "update user_info set first_open=? ,record_date=? where partner =? and mac_addr=?;";

	@SuppressWarnings("rawtypes")
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		this.session = CassandraConnection.getSession();
		this.ps = session.prepare(UPDATE_FO_RD);
	}

	public void execute(Tuple input) {
		try {
			String mac = input.getStringByField("mac");
			String partner = input.getStringByField("partner");
			String last_open = input.getStringByField("last_open");
			Date first_open = SimpleDateUtils.parseTimestamp(last_open);
			Date record_date = OnlyDateUtils.parseTimestamp(last_open);
			BoundStatement bs = Mapper.fopen_update(ps, first_open, record_date, partner, mac);
			AsyncBack.update_back(session, bs, collector, input);
		} catch (Exception e) {
			log.error("FOpen_Update_Bolt:-->{}", e.getMessage());
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

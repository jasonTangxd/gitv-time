package cn.gitv.bi.userinfo.uifmaintain.bolts;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.gitv.bi.userinfo.uifmaintain.constant.LogConst;
import cn.gitv.bi.userinfo.uifmaintain.storage.AsyncBack;
import cn.gitv.bi.userinfo.uifmaintain.storage.Mapper;
import cn.gitv.bi.userinfo.uifmaintain.utils.CassandraConnection;
import cn.gitv.bi.userinfo.uifmaintain.utils.CuratorTools;
import cn.gitv.bi.userinfo.uifmaintain.utils.StringHandle;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;

public class Distinguish_Bolt implements IRichBolt {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1172183077535819422L;
	private static CuratorFramework zkclient = CuratorTools.getSimpleCurator();
	private static final String UIF_PATH = "/uif_partner_about/partner_province";
	private OutputCollector collector = null;
	private Logger log = LoggerFactory.getLogger(Distinguish_Bolt.class);
	private static Map<String, String> partnerToProvince = null;
	private static final String MAINISNULL = "select count(main_account_id) from user_info where partner=? and mac_addr=?;";
	private PreparedStatement ps = null;
	private Session session = null;

	@SuppressWarnings("rawtypes")
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		this.session = CassandraConnection.getSession();
		this.ps = session.prepare(MAINISNULL);
		partnerToProvinceMapInit();
	}

	public void execute(Tuple input) {
		try {
			String type=input.getStringByField("enumtype");
			String Action = input.getStringByField("Action");
			String partner = input.getStringByField("partner");
			String mac = input.getStringByField("mac");
			String app_version = input.getStringByField("app_version");
			String last_open = input.getStringByField("last_open");
			if (!StringHandle.isLegalField(Action, partner, mac, app_version, last_open)) {
				collector.ack(input);
				return;
			}
			// 筛选开机信息
			if ("1".equals(Action)) {
				String province = partnerToProvince.get(partner);
				if (province == null) {
					// 1.查询zk为空的发送到noptop_bolt中
					collector.emit(LogConst.NOPTP, new Values(partner));
				}
				// 2.里面异步读取合规的数据发送到rabbit bolt
				BoundStatement read = Mapper.main_isnull(ps, partner, mac);
				AsyncBack.main_isnull(session, read, collector, input);
				// 3.数据全部发到update_bolt
				collector.emit(LogConst.UPD, new Values(type,mac, partner, province, app_version, last_open));
			} else {
				collector.ack(input);
			}
		} catch (Exception e) {
			log.error("Distinguish_Bolt:exception-->{}", e.getMessage());
			collector.fail(input);
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declareStream(LogConst.UPD, new Fields("enumtype","mac", "partner", "province", "app_version", "last_open"));
		declarer.declareStream(LogConst.RAB, new Fields("mac", "partner"));
		declarer.declareStream(LogConst.NOPTP, new Fields("partner"));
	}

	/**<p>
	 * InputStream ins =this.getClass().getClassLoader().getResourceAsStream("upp.properties");
	 * <p>
	 * // uif_partner_province.properties prop.load(ins);
	 * <p>
	 * String province = prop.getProperty(partner);
	 * <p>
	 */
	@SuppressWarnings("resource")
	private void partnerToProvinceMapInit() {
		if (partnerToProvince == null) {
			partnerToProvince = new ConcurrentHashMap<String, String>();
			try {
				List<String> partner = zkclient.getChildren().forPath(UIF_PATH);
				for (String item : partner) {
					String province = new String(zkclient.getData().forPath(UIF_PATH + "/" + item), "utf-8");
					partnerToProvince.put(item, province);
				}
				PathChildrenCache cache = new PathChildrenCache(zkclient, UIF_PATH, true);
				cache.start(StartMode.POST_INITIALIZED_EVENT);
				cache.getListenable().addListener(new PathChildListen());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	public void cleanup() {
	}

	public static class PathChildListen implements PathChildrenCacheListener {
		@Override
		public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
			switch (event.getType()) {
			case CHILD_ADDED:
			case CHILD_UPDATED:
				String full_path = event.getData().getPath();
				String partner = full_path.substring(UIF_PATH.length() + 1, full_path.length());
				String province = new String(event.getData().getData(), "utf-8");
				partnerToProvince.put(partner, province);
				break;
			case CHILD_REMOVED:
				String full_path1 = event.getData().getPath();
				String partner1 = full_path1.substring(UIF_PATH.length() + 1, full_path1.length());
				partnerToProvince.remove(partner1);
				break;
			default:
				break;
			}
		}
	}
}

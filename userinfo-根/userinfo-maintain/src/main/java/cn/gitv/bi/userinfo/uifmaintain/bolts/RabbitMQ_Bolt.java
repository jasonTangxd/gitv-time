package cn.gitv.bi.userinfo.uifmaintain.bolts;

import cn.gitv.bi.userinfo.uifmaintain.utils.CuratorTools;
import cn.gitv.bi.userinfo.uifmaintain.utils.RabbitConnection;
import com.rabbitmq.client.Channel;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class RabbitMQ_Bolt implements IRichBolt {
    /**
     *
     */
    private static List<String> partnerList = null;
    private static CuratorFramework zkclient = CuratorTools.getSimpleCurator();
    private static Logger log = LoggerFactory.getLogger(RabbitMQ_Bolt.class);
    private static final long serialVersionUID = 6519903534583116011L;
    private static final String UIF_PATH = "/uif_partner_about/builded_consumer";
    private OutputCollector collector = null;
    private static Channel channel = RabbitConnection.getChannel();

    @SuppressWarnings("rawtypes")
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        partnersListInit();
    }

    public void execute(Tuple input) {
        try {
            String mac = input.getStringByField("mac");
            String partner = input.getStringByField("partner");
            String message = mac;
            if (partnerList.contains(partner)) {
                channel.basicPublish(RabbitConnection.EXCHANGE_NAME, partner, null, message.getBytes());
            }
        } catch (Exception e) {
            log.error("", e);
            collector.fail(input);
        } finally {
            collector.ack(input);
        }

    }

    private static boolean partnerTrue(String partner) {
        String result = null;
        try {
            result = new String(zkclient.getData().forPath(UIF_PATH + "/" + partner), "utf-8");
        } catch (Exception e) {
            log.error("RabbitMQ_Bolt:partnerTrue(String partner):--{}", e.getMessage());
        }
        return result.equals("true") ? true : false;
    }

    @SuppressWarnings("resource")
    private void partnersListInit() {
        if (partnerList == null) {
            partnerList = new CopyOnWriteArrayList<String>();
            try {
                List<String> bced = zkclient.getChildren().forPath(UIF_PATH);
                for (String item : bced) {
                    if (partnerTrue(item)) {
                        partnerList.add(item);
                    }
                }
                for (String partner : partnerList) {
                    channel.queueDeclare(partner, false, false, false, null);
                    channel.queueBind(partner, RabbitConnection.EXCHANGE_NAME, partner);
                }
                PathChildrenCache cache = new PathChildrenCache(zkclient, UIF_PATH, true);
                cache.start(StartMode.POST_INITIALIZED_EVENT);
                cache.getListenable().addListener(new PathChildListen());
            } catch (Exception e) {
                log.error("RabbitMQ_Bolt:partnersListInit():--{}", e.getMessage());
            }
        }

    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }

    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

    public static class PathChildListen implements PathChildrenCacheListener {
        @Override
        public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
            switch (event.getType()) {
                case CHILD_ADDED:
                    String full_path = event.getData().getPath();
                    String bc_ed = full_path.substring(UIF_PATH.length() + 1, full_path.length());
                    partnerList.add(bc_ed);
                    break;
                case CHILD_UPDATED:
                    String full_path2 = event.getData().getPath();
                    String bc_ed2 = full_path2.substring(UIF_PATH.length() + 1, full_path2.length());
                    String flag_str = new String(event.getData().getData(), "utf-8");
                    boolean flag = flag_str.equals("true") ? true : false;
                    if (!flag) {
                        partnerList.remove(bc_ed2);
                    }
                    break;
                case CHILD_REMOVED:
                    String full_path1 = event.getData().getPath();
                    String bc_ed1 = full_path1.substring(UIF_PATH.length() + 1, full_path1.length());
                    partnerList.remove(bc_ed1);
                    break;
                default:
                    break;
            }
        }
    }

    public void cleanup() {

    }

}

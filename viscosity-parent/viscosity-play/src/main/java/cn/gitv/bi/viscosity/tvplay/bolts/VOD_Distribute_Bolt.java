package cn.gitv.bi.viscosity.tvplay.bolts;
import cn.gitv.bi.viscosity.tvplay.constant.Constant;
import cn.gitv.bi.viscosity.tvplay.utils.CuratorTools;
import cn.gitv.bi.viscosity.tvplay.utils.StringHandle;
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

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VOD_Distribute_Bolt implements IRichBolt {
    /**
     *
     */
    private static Map<String, String> partnerToProvince = null;
    private static final long serialVersionUID = 1L;
    private OutputCollector collector = null;
    private Logger log = LoggerFactory.getLogger(VOD_Distribute_Bolt.class);
    private static final String PV_PATH = "/play_viscosity/srcid_meaning";
    private static CuratorFramework zkclient = CuratorTools.getSimpleCurator();

    @SuppressWarnings("rawtypes")
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        partnerToProvinceMapInit();
    }

    public void execute(Tuple input) {
        try {
//            String play_type = input.getStringByField("play_type");
            String partner = input.getStringByField("partner");
            String mac = input.getStringByField("mac");
            String srcId = input.getStringByField("srcId");
            String chnName = input.getStringByField("chnName");
            String chnId = input.getStringByField("chnId");
            String albumName = input.getStringByField("albumName");
            String albumId = input.getStringByField("albumId");
            String playOrder = input.getStringByField("playOrder");
            String videoId = input.getStringByField("videoId");
            String timeLength = input.getStringByField("timeLength");
            String playLength = input.getStringByField("playLength");
            String logDate = input.getStringByField("logDate");
            String city = input.getStringByField("city");
            String province = input.getStringByField("province");
            if (!StringHandle.isLegalField(partner, mac, albumName, albumId, timeLength, playLength, chnName, chnId, logDate, srcId)) {
                return;
            }
            String srcName = null;
            long time_Length = Long.parseLong(timeLength);
            long play_Length = Long.parseLong(playLength);
            srcName = partnerToProvince.get(srcId);
            if (srcName != null) {
                collector.emit(Constant.VOD_TO_USER, new Values(partner, mac, srcName, chnName, chnId, albumName, albumId, playOrder, videoId,
                        play_Length, time_Length, logDate));
                collector.emit(Constant.VOD_TO_PROGRAM, new Values(partner, srcName, chnName, chnId, albumName, albumId, playOrder, videoId,
                        province, city, play_Length, time_Length, logDate));
            } else {
                collector.emit(Constant.NOSRCNAME, new Values(srcId));
            }

        } catch (Exception e) {
            log.error("VOD_Distribute_Bolt exception--{}", e.getMessage());
        } finally {
            collector.ack(input);
        }

    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declareStream(Constant.VOD_TO_USER, new Fields("partner", "mac", "srcName", "chnName", "chnId", "albumName", "albumId",
                "playOrder", "videoId", "playLength", "timeLength", "logDate"));
        declarer.declareStream(Constant.VOD_TO_PROGRAM, new Fields("partner", "srcName", "chnName", "chnId", "albumName", "albumId", "playOrder", "videoId",
                "province", "city", "playLength", "timeLength", "logDate"));
        declarer.declareStream(Constant.NOSRCNAME, new Fields("srcId"));
    }

    public Map<String, Object> getComponentConfiguration() {
        // TODO Auto-generated method stub
        return null;
    }

    @SuppressWarnings("resource")
    private void partnerToProvinceMapInit() {
        if (partnerToProvince == null) {
            partnerToProvince = new ConcurrentHashMap<String, String>();
            try {
                List<String> srcIds = zkclient.getChildren().forPath(PV_PATH);
                for (String item : srcIds) {
                    String province = new String(zkclient.getData().forPath(PV_PATH + "/" + item), "utf-8");
                    partnerToProvince.put(item, province);
                }
                PathChildrenCache cache = new PathChildrenCache(zkclient, PV_PATH, true);
                cache.start(StartMode.POST_INITIALIZED_EVENT);
                cache.getListenable().addListener(new PathChildListen());
            } catch (Exception e) {
                log.error("VOD_Distribute_Bolt exception--{}", e.getMessage());
            }
        }
    }

    public void cleanup() {
    }

    public static class PathChildListen implements PathChildrenCacheListener {
        public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
            switch (event.getType()) {
                case CHILD_ADDED:
                case CHILD_UPDATED:
                    String full_path = event.getData().getPath();
                    String srcId = full_path.substring(PV_PATH.length() + 1, full_path.length());
                    String srcName = new String(event.getData().getData(), "utf-8");
                    partnerToProvince.put(srcId, srcName);
                    break;
                case CHILD_REMOVED:
                    String full_path1 = event.getData().getPath();
                    String srcId1 = full_path1.substring(PV_PATH.length() + 1, full_path1.length());
                    partnerToProvince.remove(srcId1);
                    break;
                default:
                    break;
            }

        }

    }
}

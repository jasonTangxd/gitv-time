package cn.gitv.bi.viscosity.tvplay.bolts;

import cn.gitv.bi.viscosity.tvplay.constant.Constant;
import cn.gitv.bi.viscosity.tvplay.utils.StringHandle;
import org.apache.commons.lang.StringUtils;
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

import static cn.gitv.bi.viscosity.tvplay.constant.Constant.PBV1;

public class TopFilter_Bolt implements IRichBolt {
    /**
     *
     */
    private static final long serialVersionUID = 1172183077535819422L;
    private OutputCollector collector = null;
    private Logger log = LoggerFactory.getLogger(TopFilter_Bolt.class);

    @SuppressWarnings("rawtypes")
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    public void execute(Tuple input) {
        try {
            //获取一条记录
            String info = input.getString(0);
            if (StringUtils.isNotBlank(info)) {
                List<String> contents = StringHandle.str_split(info);
                String pingbackVersion = PBV1;//contents.get(1);
                String play_type = contents.get(0);
                switch (pingbackVersion) {
                    case PBV1:
                        if ("vod".equals(play_type)) {// 筛选点播信息
                            String partner = contents.get(3);
                            String mac = contents.get(5);
                            String srcId = contents.get(16);
                            String chnName = contents.get(15);
                            String chnId = contents.get(14);//
                            String albumName = contents.get(10);
                            String albumId = contents.get(9);//
                            String playOrder = contents.get(22);
                            String videoId = contents.get(11);//
                            String timeLength = contents.get(13);
                            String playLength = contents.get(19);
                            String logDate = contents.get(30);
                            String city = contents.get(33);
                            String province = contents.get(35);
                            collector.emit(Constant.VOD_DIS, new Values(play_type, partner, mac, srcId, chnName, chnId, albumName, albumId, playOrder, videoId, timeLength, playLength, logDate, city, province));
                        } else if ("livod".equals(play_type)) {
                            String partner = contents.get(3);
                            String mac = contents.get(5);
                            String srcId = contents.get(16);
                            String chnCode = contents.get(28);
                            String albumName = contents.get(10);
                            String playOrder = contents.get(22);
                            String timeLength = contents.get(13);
                            String playLength = contents.get(19);
                            String logDate = contents.get(33);
                            String city = contents.get(36);
                            String province = contents.get(37);
                            collector.emit(Constant.LIV_DIS, new Values(play_type, partner, mac, srcId, chnCode, albumName, playOrder, timeLength, playLength, logDate, city, province));
                        }
                        break;
                    case Constant.PBV2:
                        break;
                    default:
                        break;
                }

            }
        } catch (Exception e) {
            log.error("TopFilter_Bolt exception-->{}", e.getMessage());
        } finally {
            collector.ack(input);
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declareStream(Constant.VOD_DIS, new Fields("play_type", "partner", "mac", "srcId", "chnName", "chnId", "albumName", "albumId", "playOrder", "videoId", "timeLength",
                "playLength", "logDate", "city", "province"));
        declarer.declareStream(Constant.LIV_DIS, new Fields("play_type", "partner", "mac", "srcId", "chnCode", "albumName", "playOrder", "timeLength",
                "playLength", "logDate", "city", "province"));
    }

    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

    public void cleanup() {
    }

}

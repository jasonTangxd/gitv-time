package cn.gitv.bi.userinfo.uifmaintain.bolts;

import cn.gitv.bi.userinfo.uifmaintain.constant.LogConst;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class UpdateUinfo_Bolt implements IRichBolt {
    /**
     *
     */
    private Logger log = LoggerFactory.getLogger(UpdateUinfo_Bolt.class);
    private static final long serialVersionUID = -2838932529064300841L;
    private OutputCollector collector = null;

    @SuppressWarnings("rawtypes")
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    public void execute(Tuple input) {
        try {
            String type = input.getStringByField("enumtype");
            String mac = input.getStringByField("mac");
            String partner = input.getStringByField("partner");
            String app_version = input.getStringByField("app_version");
            String last_open = input.getStringByField("last_open");
            String province = input.getStringByField("province");
            collector.emit(LogConst.FOPEN, input, new Values(mac, partner, last_open));
            collector.emit(LogConst.APPV, input, new Values(type, mac, partner, app_version));
            collector.emit(LogConst.NOJUDGE, input, new Values(mac, partner, last_open, province));
        } catch (Exception e) {
            log.error("", e);
        } finally {
            collector.ack(input);
        }
    }

    public void cleanup() {

    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declareStream(LogConst.FOPEN, new Fields("mac", "partner", "last_open"));
        declarer.declareStream(LogConst.APPV, new Fields("enumtype", "mac", "partner", "app_version"));
        declarer.declareStream(LogConst.NOJUDGE, new Fields("mac", "partner", "last_open", "province"));
    }

    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

}

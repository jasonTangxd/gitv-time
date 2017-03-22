package cn.gitv.bi.rtvod.usercount.bolts;

import cn.gitv.bi.rtvod.usercount.utils.StringHandle;
import cn.gitv.bi.storm.loganalysis.logtransform.PingbackVersion;
import cn.gitv.bi.storm.loganalysis.logtransform.factory.PingbackContainer;
import cn.gitv.bi.storm.loganalysis.logtransform.v1.VodPlayLogTransform;
import cn.gitv.bi.storm.loganalysis.logtransform.vod.v2.VodPlay_2LogTransform;
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

/**
 * @author likang
 */
public class FilterAll implements IRichBolt {
    private static final long serialVersionUID = 1L;
    private OutputCollector collector = null;
    private Logger log = LoggerFactory.getLogger(FilterAll.class);
    private static final String V0_VOD = "V0_VOD";
    private static final String V1_VOD = "V1_VOD";

    @SuppressWarnings("rawtypes")
    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    /**
     * 筛选点播数据,接受1.0、2.0的数据
     * 下游接受(partner, mac, pt, channelName)
     */
    @Override
    public void execute(Tuple input) {
        try {
            String logInfo = input.getStringByField("str");
            if (StringUtils.isNotBlank(logInfo)) {
                List<String> values = StringHandle.str_split(logInfo);
                String type = values.get(0);
                String pingBackVersionStr = values.get(1);
                String partner = null;
                String mac = null;
                String channelName = null;
                String pt = null;
                if (V0_VOD.equals(type) || V1_VOD.equals(type)) {
                    PingbackVersion pingbackVersion = PingbackContainer.getPingbackVersion(pingBackVersionStr);
                    switch (pingbackVersion) {
                        case V1_0: {
                            VodPlayLogTransform logTransform = new VodPlayLogTransform();
                            logTransform.decodeFormatString(logInfo);
                            partner = logTransform.getPartner();
                            mac = logTransform.getMac();
                            channelName = logTransform.getChnName();
                            pt = String.valueOf(logTransform.getPlayType());
                            break;
                        }
                        case V2_0: {
                            VodPlay_2LogTransform logTransform = new VodPlay_2LogTransform();
                            logTransform.decodeFormatString(logInfo);
                            partner = logTransform.getPartner();
                            mac = logTransform.getMac();
                            channelName = logTransform.getChnName();
                            pt = String.valueOf(logTransform.getPlayType());
                            break;
                        }
                        default:
                            break;
                    }
                    if (!StringHandle.isLegalField(partner, mac, pt, channelName)) {
                        log.debug("contain null:partner--{},mac---{},pt--{},channelName---{}", partner, mac, pt, channelName);
                        return;
                    }
                    collector.emit(input, new Values(partner, mac, pt, channelName));
                }
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            collector.ack(input);
        }

    }

    @Override
    public void cleanup() {
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("partner", "mac", "pt", "channelName"));

    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

}

package cn.gitv.bi.rtliv.usercount.bolts;

import cn.gitv.bi.rtliv.usercount.utils.StringHandle;
import cn.gitv.bi.storm.loganalysis.logtransform.PingbackVersion;
import cn.gitv.bi.storm.loganalysis.logtransform.factory.PingbackContainer;
import cn.gitv.bi.storm.loganalysis.logtransform.v1.LivodPlayTransform;
import cn.gitv.bi.storm.loganalysis.logtransform.vod.v2.LivodPlay_2LogTransform;
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

public class FilterAll implements IRichBolt {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private OutputCollector collector = null;
    private Logger log = LoggerFactory.getLogger(FilterAll.class);
    private static final String LIVOD = "LIVOD";

    @SuppressWarnings("rawtypes")
    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        try {
            String logInfo = input.getStringByField("str");
            if (StringUtils.isNotBlank(logInfo)) {
                List<String> values = StringHandle.str_split(logInfo);
                String type = values.get(0);
                String pingbackVersionStr = values.get(1);
                String partner = null;
                String mac = null;
                String an = null;
                String pt = null;
                String vt = null;
                String channelCode = null;
                if (LIVOD.equals(type)) {
                    PingbackVersion pingbackVersion = PingbackContainer.getPingbackVersion(pingbackVersionStr);
                    switch (pingbackVersion) {
                        case V1_0: {
                            LivodPlayTransform logTransform = new LivodPlayTransform();
                            logTransform.decodeFormatString(logInfo);
                            partner = logTransform.getPartner();
                            mac = logTransform.getMac();
                            an = logTransform.getAlbumName();
                            pt = String.valueOf(logTransform.getPlayType());
                            vt = String.valueOf(logTransform.getVideoType());
                            channelCode = logTransform.getCc();
                            break;
                        }
                        case V2_0: {
                            LivodPlay_2LogTransform logTransform = new LivodPlay_2LogTransform();
                            logTransform.decodeFormatString(logInfo);
                            partner = logTransform.getPartner();
                            mac = logTransform.getMac();
                            an = logTransform.getAlbumName();
                            pt = String.valueOf(logTransform.getPlayType());
                            vt = String.valueOf(logTransform.getVideoType());
                            channelCode = logTransform.getCc();
                            break;
                        }
                        default:
                            break;
                    }
                    if (!StringHandle.isLegalField(partner, mac, an, pt, vt, channelCode)) {
                        log.debug("contain null:partner--{},mac---{},an--{},pt--{},vt---{},chancode---{}", partner, mac, an, pt, vt, channelCode);
                        return;
                    }
                    collector.emit(input, new Values(partner, mac, an, pt, vt, channelCode));
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
        declarer.declare(new Fields("partner", "mac", "an", "pt", "vt", "channelCode"));

    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

}

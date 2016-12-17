package cn.gitv.bi.userinfo.uifmaintain.bolts;

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

import java.util.Map;

import cn.gitv.bi.storm.loganalysis.common.VOD_TYPE;
import cn.gitv.bi.storm.loganalysis.logtransform.BaseLogTransform;
import cn.gitv.bi.storm.loganalysis.logtransform.PingbackVersion;
import cn.gitv.bi.storm.loganalysis.logtransform.cnm.A1_CNM_LogTransform;
import cn.gitv.bi.storm.loganalysis.logtransform.factory.LogCleanedTransformFactory;
import cn.gitv.bi.storm.loganalysis.logtransform.factory.PingbackContainer;
import cn.gitv.bi.storm.loganalysis.logtransform.v1.A1LogTransform;
import cn.gitv.bi.storm.loganalysis.logtransform.vod.v2.A1_2LogTransform;

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
            String info = input.getStringByField("str");
            if (StringUtils.isNotBlank(info)) {
                BaseLogTransform baseLogTransform = LogCleanedTransformFactory.decode(info);
                String pingbackVersionStr = baseLogTransform.getPingbackVersion();
                PingbackVersion pingbackVersion = PingbackContainer.getPingbackVersion(pingbackVersionStr);
                VOD_TYPE vodType = baseLogTransform.getType();
                switch (vodType) {
                    case V0_VOD:
                    case V1_VOD: {
                        switch (pingbackVersion) {
                            case V1_0: {
                                A1LogTransform transform = (A1LogTransform) baseLogTransform;
                                String Action = String.valueOf(transform.getAction());
                                String type = transform.getType().name();
                                String partner = transform.getPartner();
                                String mac = transform.getMac();
                                String app_version = transform.getVersion();
                                String last_open = transform.getLogTime();
                                collector.emit(new Values(type, Action, partner, mac, app_version, last_open));
                                break;
                            }
                            case V2_0: {
                                A1_2LogTransform transform = (A1_2LogTransform) baseLogTransform;
                                String Action = String.valueOf(transform.getAction());
                                String type = transform.getType().name();
                                String partner = transform.getPartner();
                                String mac = transform.getMac();
                                String app_version = transform.getVersion();
                                String last_open = transform.getLogTime();
                                collector.emit(new Values(type, Action, partner, mac, app_version, last_open));
                                break;
                            }
                            default:
                                break;
                        }
                        break;
                    }
                    case LIVOD: {
                        switch (pingbackVersion) {
                            case V1_0: {
                                A1LogTransform transform = (A1LogTransform) baseLogTransform;
                                String Action = String.valueOf(transform.getAction());
                                String type = transform.getType().name();
                                String partner = transform.getPartner();
                                String mac = transform.getMac();
                                String app_version = transform.getVersion();
                                String last_open = transform.getLogTime();
                                collector.emit(new Values(type, Action, partner, mac, app_version, last_open));
                                break;
                            }
                            default:
                                break;
                        }
                        break;
                    }
                    case CNM: {
                        switch (pingbackVersion) {
                            case V1_0: {
                                A1_CNM_LogTransform transform = (A1_CNM_LogTransform) baseLogTransform;
                                String Action = String.valueOf(transform.getAction());
                                String type = transform.getType().name();
                                String partner = transform.getPartner();
                                String mac = transform.getMac();
                                String app_version = transform.getVersion();
                                String last_open = transform.getLogTime();
                                collector.emit(new Values(type, Action, partner, mac, app_version, last_open));
                                break;
                            }
                            default:
                                break;
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            log.error("TopFilter_Bolt exception-->", e);
        } finally {
            collector.ack(input);
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("enumtype", "Action", "partner", "mac", "app_version", "last_open"));
    }

    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

    public void cleanup() {
    }

}

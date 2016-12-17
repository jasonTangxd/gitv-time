package cn.gitv.bi.userinfo.appupdate.bolts;

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
import cn.gitv.bi.storm.loganalysis.logtransform.factory.LogCleanedTransformFactory;
import cn.gitv.bi.storm.loganalysis.logtransform.factory.PingbackContainer;
import cn.gitv.bi.storm.loganalysis.logtransform.v1.A10LogTransform;
import cn.gitv.bi.storm.loganalysis.logtransform.vod.v2.A10_2LogTransform;

import static java.awt.SystemColor.info;

public class TopFilter_Bolt implements IRichBolt {

    private static final long serialVersionUID = 1688757578656330072L;
    private OutputCollector collector = null;
    private Logger log = LoggerFactory.getLogger(TopFilter_Bolt.class);

    @SuppressWarnings("rawtypes")
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    public void execute(Tuple input) {
        String logInfo = input.getStringByField("str");
        try {
            if (StringUtils.isNotBlank(logInfo)) {
                BaseLogTransform baseLogTransform = LogCleanedTransformFactory.decode(logInfo);
                VOD_TYPE vodType = baseLogTransform.getType();
                String pingbackVersionStr = baseLogTransform.getPingbackVersion();
                PingbackVersion pingbackVersion = PingbackContainer.getPingbackVersion(pingbackVersionStr);
                switch (vodType) {
                    case V0_VOD:
                    case V1_VOD: {
                        switch (pingbackVersion) {
                            case V1_0: {
                                A10LogTransform transform = (A10LogTransform) baseLogTransform;
                                String type = transform.getType().name();
                                String partner = transform.getPartner();
                                String mac = transform.getMac();
                                String app_version = transform.getVersion();
                                String update_res = String.valueOf(transform.getResult());// 升级结果
                                String logtime = transform.getLogTime();
                                collector.emit(input, new Values(type, partner, mac, app_version, update_res, logtime));
                                break;
                            }
                            case V2_0: {
                                A10_2LogTransform transform = (A10_2LogTransform) baseLogTransform;
                                String type = transform.getType().name();
                                String partner = transform.getPartner();
                                String mac = transform.getMac();
                                String app_version = transform.getVersion();
                                String update_res = String.valueOf(transform.getResult());// 升级结果
                                String logtime = transform.getLogTime();
                                collector.emit(input, new Values(type, partner, mac, app_version, update_res, logtime));
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
                                A10LogTransform transform = (A10LogTransform) baseLogTransform;
                                String type = transform.getType().name();
                                String partner = transform.getPartner();
                                String mac = transform.getMac();
                                String app_version = transform.getVersion();
                                String update_res = String.valueOf(transform.getResult());// 升级结果
                                String logtime = transform.getLogTime();
                                collector.emit(input, new Values(type, partner, mac, app_version, update_res, logtime));
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
                                A10LogTransform transform = (A10LogTransform) baseLogTransform;
                                String type = transform.getType().name();
                                String partner = transform.getPartner();
                                String mac = transform.getMac();
                                String app_version = transform.getVersion();
                                String update_res = String.valueOf(transform.getResult());// 升级结果
                                String logtime = transform.getLogTime();
                                collector.emit(input, new Values(type, partner, mac, app_version, update_res, logtime));
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
            log.error("logInfo is {},e:{}", logInfo,e);
        } finally {
            collector.ack(input);
        }

    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("enumtype", "partner", "mac", "app_version", "update_res", "logtime"));
    }

    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

    public void cleanup() {
    }
}

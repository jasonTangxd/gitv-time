package cn.gitv.bi.launcher.tohdfs.bolt;
import cn.gitv.bi.launcher.beanparse.utils.SimpleDateUtils;
import cn.gitv.bi.launcher.beanparse.logbean.*;
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

import static cn.gitv.bi.launcher.beanparse.constant.Constant.topicPrefix;

/**
 * Created by Kang on 2016/12/1.
 */
public class PartitionBolt implements IRichBolt {
    private OutputCollector collector;
    private static final Logger LOG = LoggerFactory.getLogger(PartitionBolt.class);

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    public long stringToLongTS(String TS) {
        return SimpleDateUtils.parseTimestamp(TS).getTime();
    }

    @Override
    public void execute(Tuple input) {
        String topic = input.getStringByField("topic");
        String data = input.getStringByField("data");
        String Action = topic.substring(topicPrefix.length(), topic.length());
        String LogDate = null;
        long TS = 0;
        switch (Action) {
            case "100":
                LogBean_0 log0 = new LogBean_0();
                if (log0.stringToBeanInit(data)) {
                    LogDate = log0.getRecord_day();
                    TS = stringToLongTS(log0.getTimestamp());
                } else {
                    collector.ack(input);
                    return;
                }
                break;
            case "101":
                LogBean_1 log1 = new LogBean_1();
                if (log1.stringToBeanInit(data)) {
                    LogDate = log1.getRecord_day();
                    TS = stringToLongTS(log1.getTimestamp());
                } else {
                    collector.ack(input);
                    return;
                }
                break;
            case "102":
                LogBean_2 log2 = new LogBean_2();
                if (log2.string4ConApkToBeanInit(data)) {
                    LogDate = log2.getRecord_day();
                    TS = stringToLongTS(log2.getTimestamp());
                } else {
                    collector.ack(input);
                    return;
                }
                break;
            case "103":
                LogBean_3 log3 = new LogBean_3();
                if (log3.string4ConApkToBeanInit(data)) {
                    LogDate = log3.getRecord_day();
                    TS = stringToLongTS(log3.getTimestamp());
                } else {
                    collector.ack(input);
                    return;
                }
                break;
            case "104":
                LogBean_4 log4 = new LogBean_4();
                if (log4.stringToBeanInit(data)) {
                    LogDate = log4.getRecord_day();
                    TS = stringToLongTS(log4.getTimestamp());
                } else {
                    collector.ack(input);
                    return;
                }
                break;
        }
        collector.emit(input, new Values(Action, LogDate, TS, data));
        LOG.info("Action is {},LogDate is {}, TS is {} ,and data is {}", Action, LogDate, TS, data);
    }

    @Override
    public void cleanup() {

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("Action", "LogDate", "TS", "Content"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}

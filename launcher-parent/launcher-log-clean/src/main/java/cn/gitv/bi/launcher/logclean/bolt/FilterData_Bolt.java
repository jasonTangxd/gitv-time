package cn.gitv.bi.launcher.logclean.bolt;

import cn.gitv.bi.launcher.beanparse.apkparse.ApkBean;
import cn.gitv.bi.launcher.beanparse.apkparse.ApkParseUtils;
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

public class FilterData_Bolt implements IRichBolt {

    /**
     *
     */
    private static final long serialVersionUID = 4482942118048306280L;
    private OutputCollector collector;
    private Logger log = LoggerFactory.getLogger(FilterData_Bolt.class);

    @SuppressWarnings("rawtypes")
    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        try {
            String Action = input.getStringByField("Action");
            String content = input.getStringByField("Content");
            /**
             * 这个switch可以保证只发出去100、101、102、103、104的action
             * */
            switch (Action) {
                case "100":
                    LogBean_0 log0 = new LogBean_0();
                    if (log0.stringToBeanInit(content)) {
                        if (log0.fieldsFilter()) {
                            collector.emit(input, new Values(Action, content));
                        }
                    }
                    break;
                case "101":
                    LogBean_1 log1 = new LogBean_1();
                    if (log1.stringToBeanInit(content)) {
                        if (log1.fieldsFilter()) {
                            collector.emit(input, new Values(Action, content));
                        }
                    }
                    break;
                case "104":
                    LogBean_4 log4 = new LogBean_4();
                    if (log4.stringToBeanInit(content)) {
                        if (log4.fieldsFilter()) {
                            collector.emit(input, new Values(Action, content));
                        }
                    }
                    break;
                case "102"://解析apk
                    LogBean_2 log2 = new LogBean_2();
                    if (log2.stringToBeanInit(content)) {
                        if (log2.fieldsFilter()) {
                            String APK = log2.getAPK();
//                        String json = APK.replaceAll("\\\\x22", "\"");
                            String json = APK.replaceAll("'", "\"");
                            ApkBean apkBean = ApkParseUtils.jsonToBean(json);
                            if (apkBean == null) {
                                //为空说明解析存在异常，则不下发，停止，但是不fail
                                collector.ack(input);
                                return;
                            }
                            String newContent = log2.toString(apkBean.toString());
                            collector.emit(input, new Values(Action, newContent));
                        }
                    }
                    break;
                case "103"://解析apk
                    LogBean_3 log3 = new LogBean_3();
                    if (log3.stringToBeanInit(content)) {
                        if (log3.fieldsFilter()) {
                            String APK = log3.getAPK();
                            String json = APK.replaceAll("'", "\"");
//                        String json = APK.replaceAll("\\\\x22", "\"");
                            ApkBean apkBean = ApkParseUtils.jsonToBean(json);
                            if (apkBean == null) {
                                collector.ack(input);
                                return;
                            }
                            String newContent = log3.toString(apkBean.toString());
                            collector.emit(input, new Values(Action, newContent));
                        }
                    }
                    break;
                default:
                    break;
            }
            collector.ack(input);
        } catch (Exception e) {
            collector.fail(input);
            log.error("FilterData_Bolt ERROR-->{}", e.getMessage());
        }
    }

    @Override
    public void cleanup() {
        // TODO Auto-generated method stub

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("Action", "Content"));

    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        // TODO Auto-generated method stub
        return null;
    }

}

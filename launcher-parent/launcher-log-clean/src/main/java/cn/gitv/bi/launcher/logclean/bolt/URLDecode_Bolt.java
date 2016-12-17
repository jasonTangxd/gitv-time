package cn.gitv.bi.launcher.logclean.bolt;

import cn.gitv.bi.launcher.beanparse.logbean.LogBean;
import cn.gitv.bi.launcher.beanparse.utils.StringHandle;
import cn.gitv.bi.launcher.logclean.mapper.FieldMapToBean;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import org.apache.commons.lang3.StringUtils;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class URLDecode_Bolt implements IRichBolt {
    private static final long serialVersionUID = 8292483664386748909L;
    private OutputCollector collector = null;
    private Logger log = LoggerFactory.getLogger(URLDecode_Bolt.class);
    private Session session = null;
    private PreparedStatement ps = null;
//    private static final String data_exception = "insert into url_data_ecp(utl_data,happen_time,what_exception) values (?,?,?)";

    @SuppressWarnings("rawtypes")
    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
//        this.session = CassandraConnection.getSession();
//        ps = session.prepare(data_exception);
    }

    @Override
    public void execute(Tuple input) {
        String ip = input.getStringByField("ip");
        String time = input.getStringByField("time");
        String data = input.getStringByField("data");
        try {
            if (StringHandle.isLegalField(ip, time, data)) {
                //取出url中的标准参数串
                String url_analyze = data.substring(data.indexOf("?") + 1, data.length());
                MultiMap<String> multiMap = new MultiMap<>();
                //url_analyze -> [MultiMap]
                UrlEncoded.decodeTo(url_analyze, multiMap, "utf-8", 1000);
                String Action = multiMap.getValue("A", 0);
                if (StringUtils.isBlank(Action)) {
                    //如果action是空,后续操作结束
                    collector.ack(input);
                    return;
                }
                //values -> logbean
                LogBean logBean = FieldMapToBean.getLogBean(multiMap, ip, time);
                //logbean -> String
                if (logBean != null) {
                    String launcher_log = logBean.toString();
                    collector.emit(input, new Values(Action, launcher_log));
                }
                collector.ack(input);
            } else {
                //ip, time, data有为空的抛弃，不重发
                collector.ack(input);
            }
        } catch (Exception e) {
//            BoundStatement bs=Mapper.exception_record(ps, data, new Date(System.currentTimeMillis()), e.getMessage());
//            AsyncBack.update_back(session, bs, collector, input);
            //url错误解析的发送elk，不重发
            collector.ack(input);
            log.error("URLEncode_Bolt ERROR-->{}", e.getMessage());
        }
    }

    @Override
    public void cleanup() {
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("Action", "Content"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}

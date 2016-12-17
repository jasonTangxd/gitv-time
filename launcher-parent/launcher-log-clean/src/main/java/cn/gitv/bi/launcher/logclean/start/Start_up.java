package cn.gitv.bi.launcher.logclean.start;

import cn.gitv.bi.launcher.logclean.bolt.FilterData_Bolt;
import cn.gitv.bi.launcher.logclean.bolt.URLDecode_Bolt;
import cn.gitv.bi.launcher.logclean.constant.MyProperties;
import cn.gitv.bi.launcher.logclean.scheme.NginxScheme;
import cn.gitv.bi.launcher.logclean.tokafka.ActionKafkaTopicSelector;
import cn.gitv.bi.launcher.logclean.tokafka.LogKafkaMapper;
import kafka.api.OffsetRequest;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.kafka.BrokerHosts;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.kafka.bolt.KafkaBolt;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.utils.Utils;

import java.util.Properties;

import static cn.gitv.bi.launcher.logclean.constant.MyProperties.BOOTSTRAP;
import static cn.gitv.bi.launcher.logclean.constant.MyProperties.Serializer;


public class Start_up {

    private static SpoutConfig spoutConfBuild() {
        BrokerHosts brokerHosts = new ZkHosts(MyProperties.ZK_HOST_PORT);
        SpoutConfig spoutConfig = new SpoutConfig(brokerHosts, MyProperties.CONSUMER_TOPIC, "/storm", MyProperties.SPOUT_INZK);
        spoutConfig.scheme = new SchemeAsMultiScheme(new NginxScheme());
        spoutConfig.startOffsetTime = OffsetRequest.LatestTime();// 表示从kafka最新接手的数据开始
        return spoutConfig;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void main(String[] args) {
        SpoutConfig spoutConfig = spoutConfBuild();
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("launcher_log", new KafkaSpout(spoutConfig), 3);
        builder.setBolt("url_decode", new URLDecode_Bolt(), 3).shuffleGrouping("launcher_log");
        builder.setBolt("filter_data", new FilterData_Bolt(), 3).shuffleGrouping("url_decode");
        //set kafka bolt
        KafkaBolt kafkaBolt = new KafkaBolt();
        Properties kafkaProperties = producerConfig();
        kafkaBolt.withTopicSelector(new ActionKafkaTopicSelector()).withTupleToKafkaMapper(new LogKafkaMapper()).withProducerProperties(kafkaProperties);
        builder.setBolt("to_kafka", kafkaBolt, 3).shuffleGrouping("filter_data");
        Config conf = new Config();
        conf.setMaxSpoutPending(2000);
        conf.setMessageTimeoutSecs(60);
        if (args == null || args.length == 0) {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("topo", conf, builder.createTopology());
            Utils.sleep(100000);
            cluster.killTopology("topo");
            cluster.shutdown();
        } else {
            conf.setNumWorkers(3);
            try {
                StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Properties producerConfig() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, Serializer);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, Serializer);
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        return props;
    }
}

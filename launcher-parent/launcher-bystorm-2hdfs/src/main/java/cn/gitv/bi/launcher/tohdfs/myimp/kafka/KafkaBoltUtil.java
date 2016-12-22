package cn.gitv.bi.launcher.tohdfs.myimp.kafka;

import org.apache.storm.kafka.spout.*;
import org.apache.storm.tuple.Fields;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import static cn.gitv.bi.launcher.tohdfs.constant.MyProperties.BOOTSTRAP;
import static cn.gitv.bi.launcher.tohdfs.constant.MyProperties.TOPICS;

/**
 * Created by Kang on 2016/12/1.
 */
public class KafkaBoltUtil {
    public static Map<String, Object> newKafkaConsumerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(KafkaSpoutConfig.Consumer.BOOTSTRAP_SERVERS, BOOTSTRAP);
        props.put(KafkaSpoutConfig.Consumer.GROUP_ID, "launcher-to-hdfs");
        props.put(KafkaSpoutConfig.Consumer.KEY_DESERIALIZER, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(KafkaSpoutConfig.Consumer.VALUE_DESERIALIZER, "org.apache.kafka.common.serialization.StringDeserializer");
        return props;
    }

    public static KafkaSpoutRetryService newRetryService() {
        return new KafkaSpoutRetryExponentialBackoff(new KafkaSpoutRetryExponentialBackoff.TimeInterval(500L, TimeUnit.MICROSECONDS),
                KafkaSpoutRetryExponentialBackoff.TimeInterval.milliSeconds(2),
                Integer.MAX_VALUE, KafkaSpoutRetryExponentialBackoff.TimeInterval.seconds(10));
    }

    public static KafkaSpoutTuplesBuilder<String, String> newTuplesBuilder() {
        return new KafkaSpoutTuplesBuilderNamedTopics.Builder<>(new TopicsTupleBuilder<String, String>(TOPICS))
                .build();
    }

    public static KafkaSpoutStreams newKafkaSpoutStreams() {
        return new KafkaSpoutStreamsNamedTopics.Builder(new Fields("topic", "data"), TOPICS).build();
    }

}

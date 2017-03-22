package cn.gitv.bi.launcher.tohdfs.myimp.kafka;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.storm.kafka.spout.*;
import org.apache.storm.tuple.Values;

import java.util.List;

/**
 * Created by Kang on 2016/12/1.
 */
public class TopicsTupleBuilder<K,V> extends KafkaSpoutTupleBuilder<K, V> {
    public TopicsTupleBuilder(String... topics) {
        super(topics);
    }

    @Override
    public List<Object> buildTuple(ConsumerRecord<K, V> consumerRecord) {
        return new Values(consumerRecord.topic(), consumerRecord.value());
    }
}

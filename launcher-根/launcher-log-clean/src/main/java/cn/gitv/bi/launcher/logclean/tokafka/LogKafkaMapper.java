package cn.gitv.bi.launcher.logclean.tokafka;

import org.apache.storm.kafka.bolt.mapper.TupleToKafkaMapper;
import org.apache.storm.tuple.Tuple;

@SuppressWarnings("rawtypes")
public class LogKafkaMapper implements TupleToKafkaMapper {

    /**
     *
     */
    private static final long serialVersionUID = 4060057324897232379L;


    public Object getMessageFromTuple(Tuple tuple) {
        return tuple.getStringByField("Content");
    }


    @Override
    public Object getKeyFromTuple(Tuple tuple) {
        return null;
    }

}

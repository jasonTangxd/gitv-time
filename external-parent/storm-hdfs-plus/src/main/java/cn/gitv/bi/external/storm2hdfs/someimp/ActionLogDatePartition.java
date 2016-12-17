package cn.gitv.bi.external.storm2hdfs.someimp;

import org.apache.hadoop.fs.Path;
import cn.gitv.bi.external.storm2hdfs.common.Partitioner;
import org.apache.storm.tuple.Tuple;

/**
 * Created by Kang on 2016/12/1.
 */
public class ActionLogDatePartition implements Partitioner {
    //根据两个字段分区
    @Override
    public String getPartitionPath(Tuple tuple) {
        String Action = tuple.getStringByField("Action");
        String LogDate = tuple.getStringByField("LogDate");
        return Path.SEPARATOR + Action + Path.SEPARATOR + LogDate;
    }
}

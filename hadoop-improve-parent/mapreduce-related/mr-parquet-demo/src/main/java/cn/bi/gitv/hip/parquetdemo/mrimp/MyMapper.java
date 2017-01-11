package cn.bi.gitv.hip.parquetdemo.mrimp;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.parquet.example.data.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

/**
 * Created by Kang on 2016/12/30.
 */
public class MyMapper extends Mapper<Object, Group, NullWritable, Text> {
    private static Logger LOG = LoggerFactory.getLogger(MyMapper.class);

    @Override
    protected void map(Object key, Group value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        LOG.info("this is the line i need {}", line);
    }
}

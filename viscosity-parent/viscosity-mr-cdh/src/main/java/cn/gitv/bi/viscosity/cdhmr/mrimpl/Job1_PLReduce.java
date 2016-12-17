package cn.gitv.bi.viscosity.cdhmr.mrimpl;

import cn.gitv.bi.viscosity.cdhmr.constant.Constant;
import cn.gitv.bi.viscosity.cdhmr.utils.StringHandle;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Job1_PLReduce extends Reducer<Text, Text, NullWritable, Text> {
    private static final Text mvalue = new Text();
    private static Logger log = LoggerFactory.getLogger(Job1_PLReduce.class);

    /**
     * [key]:NullWritable
     * [value]:partner_code+chn_id+album_name+album_id+String.valueOf(totalPL)+String.valueOf(mac_set.size())+time_length
     * AH_CMCC|CCTV-1|新闻联播|1002|2302|110|30
     */
    @Override
    protected void reduce(Text arg0, Iterable<Text> arg1, Reducer<Text, Text, NullWritable, Text>.Context arg2) throws IOException, InterruptedException {
        Iterator<Text> it = arg1.iterator();
        Set<String> mac_set = new HashSet<String>();
        int totalPL = 0;
        String time_length = null;
        while (it.hasNext()) {
            Text line = it.next();
            List<String> messages = StringHandle.str_token_split(line.toString(), Constant.SEPARATOR);
            String dev_mac = messages.get(0);
            time_length = messages.get(1);
            int play_length = Integer.parseInt(messages.get(2));
            totalPL += play_length;
            mac_set.add(dev_mac);
        }
        String value = StringHandle.str_join(arg0.toString(), String.valueOf(totalPL), String.valueOf(mac_set.size()), time_length);
//        log.info("Job1_PLReduce write value is -->{}", value);
        mvalue.set(value);
        arg2.write(NullWritable.get(), mvalue);
    }

}

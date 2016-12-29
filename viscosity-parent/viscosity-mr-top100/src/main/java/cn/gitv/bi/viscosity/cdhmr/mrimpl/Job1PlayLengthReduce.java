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

public class Job1PlayLengthReduce extends Reducer<Text, Text, NullWritable, Text> {
    private static final Text mValue = new Text();
    private static Logger log = LoggerFactory.getLogger(Job1PlayLengthReduce.class);

    /**
     * [key]:NullWritable
     * [value]:partner_code+chn_id+album_name+album_id+String.valueOf(totalPL)+String.valueOf(mac_set.size())+time_length
     * AH_CMCC|CCTV-1|新闻联播|1002|2302|110|30
     */
    @Override
    protected void reduce(Text arg0, Iterable<Text> arg1, Reducer<Text, Text, NullWritable, Text>.Context context) throws IOException, InterruptedException {
        Iterator<Text> it = arg1.iterator();
        Set<String> macSet = new HashSet<String>();
        long totalPL = 0;
        String timeLength = null;
        while (it.hasNext()) {
            Text line = it.next();
            List<String> messages = StringHandle.str_token_split(line.toString(), Constant.SEPARATOR);
            String devMac = messages.get(0);
            timeLength = messages.get(1);
            int playLength = Integer.parseInt(messages.get(2));
            totalPL += playLength;
            macSet.add(devMac);
        }
        String value = StringHandle.str_join(arg0.toString(), String.valueOf(totalPL), String.valueOf(macSet.size()), timeLength);
//        log.info("Job1PlayLengthReduce write value is -->{}", value);
        mValue.set(value);
        context.write(NullWritable.get(), mValue);
    }

}

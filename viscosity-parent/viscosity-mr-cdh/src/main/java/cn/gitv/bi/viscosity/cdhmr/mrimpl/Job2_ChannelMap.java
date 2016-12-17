package cn.gitv.bi.viscosity.cdhmr.mrimpl;

import cn.gitv.bi.viscosity.cdhmr.constant.Constant;
import cn.gitv.bi.viscosity.cdhmr.utils.StringHandle;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class Job2_ChannelMap extends Mapper<Object, Text, Text, Text> {
    private static Logger log = LoggerFactory.getLogger(Job2_ChannelMap.class);
    private static final Text mkey = new Text();
    private static final Text mvalue = new Text();

    /**
     * [key]:partner_code, chn_id
     * [value]:album_name+album_id+play_length_s+mac_num+time_length
     */
    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        List<String> line = StringHandle.str_token_split(value.toString(), Constant.SEPARATOR);
        String partner_code = line.get(0);
        String chn_id = line.get(1);
        String album_name = line.get(2);
        String album_id = line.get(3);
        String play_length_s = line.get(4);
        String mac_num = line.get(5);
        String time_length = line.get(6);
        if (StringHandle.isLegalField(partner_code, chn_id, album_name, play_length_s, mac_num)) {
            String key_merge = StringHandle.str_join(partner_code, chn_id);
            mkey.set(key_merge);
            //
            String value_merge = StringHandle.str_join(album_name, album_id, play_length_s, mac_num, time_length);
            mvalue.set(value_merge);
            context.write(mkey, mvalue);
        }
    }
}
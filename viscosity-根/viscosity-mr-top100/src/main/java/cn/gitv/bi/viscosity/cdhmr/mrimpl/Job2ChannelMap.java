package cn.gitv.bi.viscosity.cdhmr.mrimpl;

import cn.gitv.bi.viscosity.cdhmr.constant.Constant;
import cn.gitv.bi.viscosity.cdhmr.utils.StringHandle;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class Job2ChannelMap extends Mapper<Object, Text, Text, Text> {
    private static Logger log = LoggerFactory.getLogger(Job2ChannelMap.class);
    private static final Text mkey = new Text();
    private static final Text mvalue = new Text();

    /**
     * [key]:partner_code, chn_id
     * [value]:album_name+album_id+play_length_s+mac_num+time_length
     */
    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        List<String> line = StringHandle.str_token_split(value.toString(), Constant.SEPARATOR);
        String partnerCode = line.get(0);
        String chnId = line.get(1);
        String albumName = line.get(2);
        String albumId = line.get(3);
        String playLengthS = line.get(4);
        String macNum = line.get(5);
        String timeLength = line.get(6);
        if (StringHandle.isLegalField(partnerCode, chnId, albumName, playLengthS, macNum)) {
            String keyMerge = StringHandle.str_join(partnerCode, chnId);
            mkey.set(keyMerge);
            //
            String valueMerge = StringHandle.str_join(albumName, albumId, playLengthS, macNum, timeLength);
            mvalue.set(valueMerge);
            context.write(mkey, mvalue);
        }
    }
}
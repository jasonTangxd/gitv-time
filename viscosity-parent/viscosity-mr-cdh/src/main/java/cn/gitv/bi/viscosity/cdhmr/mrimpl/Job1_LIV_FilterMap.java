package cn.gitv.bi.viscosity.cdhmr.mrimpl;

import cn.gitv.bi.viscosity.cdhmr.constant.Constant;
import cn.gitv.bi.viscosity.cdhmr.start.Start_up;
import cn.gitv.bi.viscosity.cdhmr.utils.StringHandle;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class Job1_LIV_FilterMap extends Mapper<Object, Text, Text, Text> {
    private static Logger log = LoggerFactory.getLogger(Job1_LIV_FilterMap.class);
    private static final Text mkey = new Text();
    private static final Text mvalue = new Text();
    private static final Pattern pt = Pattern.compile("|");

    /**
     * [key]:partner_code+chn_code+album_name+album_id
     * [value]:dev_mac+time_length_s+play_length_s
     */
    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        List<String> line = StringHandle.str_token_split(value.toString(), Constant.SEPARATOR);
//        log.info("line size -->{}---->line is{}", line.size(), line.toString());
        if (line.size() == 46) {
            String chn_code = line.get(0);
            String action_s = line.get(5);//filter
            String dev_mac = line.get(7);
            String partner_code = line.get(8);
            String time_length_s = line.get(13);
            String play_length_s = line.get(14);
            String album_id = line.get(15);// all is null
            String album_name = StringHandle.getPrintStringAndReplaceAll(line.get(16), pt, "");
            String play_type_s = line.get(28);//filter
            if (StringHandle.isLegalField(chn_code, action_s, dev_mac, partner_code, time_length_s, play_length_s, album_name, play_type_s)) {
                int action = Integer.parseInt(action_s);
                int play_length = Integer.parseInt(play_length_s);
                int play_type = Integer.parseInt(play_type_s);
                if (filter(action, play_length, play_type)) {
                    String key_merge = StringHandle.str_join(partner_code, chn_code, album_name, album_id);
                    mkey.set(key_merge);
                    String value_merge = StringHandle.str_join(dev_mac, time_length_s, play_length_s);
//                    log.info("key_merge -->{},   value_merge-->{}", key_merge, value_merge);
                    mvalue.set(value_merge);
                    context.write(mkey, mvalue);
                }
            }
        }
    }

    private boolean filter(int action, int play_length, int play_type) {
        if (action == 5 && (play_type == 1 || play_type == 2) && play_length > 4 && play_length < 100000) {
            return true;
        }
        return false;
    }

}
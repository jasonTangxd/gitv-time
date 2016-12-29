package cn.gitv.bi.viscosity.cdhmr.mrimpl;

import cn.gitv.bi.viscosity.cdhmr.constant.Constant;
import cn.gitv.bi.viscosity.cdhmr.utils.StringHandle;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class Job1VodFilterMap extends Mapper<Object, Text, Text, Text> {
    private static Logger log = LoggerFactory.getLogger(Job1VodFilterMap.class);
    private static final Text mkey = new Text();
    private static final Text mvalue = new Text();
    private static final Pattern pt = Pattern.compile("|");

    /**
     * [key]:partner_code+chn_id+album_name+album_id
     * [value]:dev_mac+time_length_s+play_length_s
     */
    @Override
    protected void map(Object key, Text value, Mapper<Object, Text, Text, Text>.Context context) throws IOException, InterruptedException {
        List<String> line = StringHandle.str_token_split(value.toString(), Constant.SEPARATOR);
        if (line.size() == 44) {
            String actionS = line.get(3);
            String devMac = line.get(5);
            String partnerCode = line.get(6);
            String timeLengthS = line.get(11);
            String playLengthS = line.get(12);
            String albumId = line.get(13);
            String albumName = StringHandle.getPrintStringAndReplaceAll(line.get(14), pt, "");
            String chnId = line.get(17);
            String playTypeS = line.get(26);
            //filter
            if (StringHandle.isLegalField(actionS, devMac, partnerCode, timeLengthS, playLengthS, albumName, albumId, chnId, playTypeS)) {
                int action = Integer.parseInt(actionS);
                int playLength = NumberUtils.toInt(playLengthS, 0);
                int timeLength = NumberUtils.toInt(timeLengthS, 1);
                int playType = Integer.parseInt(playTypeS);
                if (filter1(action, playType, playLength)) {
                    playLength = filter2(playLength, timeLength);
                    String keyMerge = StringHandle.str_join(partnerCode, chnId, albumName, albumId);
                    mkey.set(keyMerge);
                    String valueMerge = StringHandle.str_join(devMac, timeLengthS, playLength + "");
                    mvalue.set(valueMerge);
                    context.write(mkey, mvalue);
                }
            }
        }
    }

    private boolean filter1(int action, int playType, int playLength) {
        if (action == 5 && (playType == 1 || playType == 2) && playLength > 4) {
            return true;
        }
        return false;
    }

    private int filter2(int playLength, int timeLength) {
        if (playLength > timeLength) {
            return timeLength;
        } else {
            return playLength;
        }
    }


}
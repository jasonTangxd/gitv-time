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

public class Job1LivFilterMap extends Mapper<Object, Text, Text, Text> {
    private static Logger log = LoggerFactory.getLogger(Job1LivFilterMap.class);
    private static final Text mKey = new Text();
    private static final Text mValue = new Text();
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
            String chnCode = line.get(0);
            String actionS = line.get(5);//filter
            String devMac = line.get(7);
            String partnerCode = line.get(8);
            String timeLengthS = line.get(13);
            String playLengthS = line.get(14);
            String albumId = line.get(15);// all is null
            String albumName = StringHandle.getPrintStringAndReplaceAll(line.get(16), pt, "");
            String playTypeS = line.get(28);//filter
            if (StringHandle.isLegalField(chnCode, actionS, devMac, partnerCode, timeLengthS, playLengthS, albumName, playTypeS)) {
                int action = Integer.parseInt(actionS);
                int playLength = NumberUtils.toInt(playLengthS, 0);
                int timeLength = NumberUtils.toInt(timeLengthS, 1);
                int playType = Integer.parseInt(playTypeS);
                if (filter1(action, playLength, playType)) {
                    playLength = filter2(playLength, timeLength);
                    String keyMerge = StringHandle.str_join(partnerCode, chnCode, albumName, albumId);
                    mKey.set(keyMerge);
                    String valueMerge = StringHandle.str_join(devMac, timeLengthS, playLength + "");
                    mValue.set(valueMerge);
                    context.write(mKey, mValue);
                }
            }
        }
    }

    private boolean filter1(int action, int playLength, int playType) {
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
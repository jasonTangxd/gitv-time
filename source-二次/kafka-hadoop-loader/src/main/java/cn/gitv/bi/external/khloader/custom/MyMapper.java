package cn.gitv.bi.external.khloader.custom;

import cn.gitv.bi.external.khloader.inf.HadoopJobMapper;
import cn.gitv.bi.external.khloader.utils.MapInputParse;
import cn.gitv.bi.external.khloader.utils.StringHandle;
import org.apache.hadoop.io.BytesWritable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Kang on 2016/12/27.
 */
public class MyMapper extends HadoopJobMapper {
    @Override
    public Date parseDateFromData(BytesWritable outputValue) {
        String line = MapInputParse.bytesWritable2String(outputValue);
        List<String> strings = StringHandle.str_token_split(line, "|");
        String logTime = strings.get(strings.size() - 1);
//                LOG.warn("HadoopJobMap`s logtime is {}", logTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(logTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}

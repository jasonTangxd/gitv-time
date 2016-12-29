package cn.bi.gitv.dayjob.example.start;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.List;

public class MyMap extends Mapper<Object, Text, Text, Text> {
    String SEPARATOR = "|";
    private static final Text mkey = new Text();
    private static final Text mvalue = new Text();

    /**
     * [key]:partner_code+chn_id+album_name+album_id
     * [value]:dev_mac+time_length_s+play_length_s
     */
    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        List<String> line = StringHandle.str_token_split(value.toString(), SEPARATOR);
        if (line.size() == 44) {
            mkey.set(value.toString().length() + "");
            mvalue.set(value.toString());
            context.write(mkey, mvalue);
        }
    }
}
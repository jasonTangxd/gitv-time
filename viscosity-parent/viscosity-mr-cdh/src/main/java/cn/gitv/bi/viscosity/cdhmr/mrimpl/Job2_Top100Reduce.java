package cn.gitv.bi.viscosity.cdhmr.mrimpl;

import cn.gitv.bi.viscosity.cdhmr.bean.Album_PL;
import cn.gitv.bi.viscosity.cdhmr.bean.Album_ZB;
import cn.gitv.bi.viscosity.cdhmr.constant.Constant;
import cn.gitv.bi.viscosity.cdhmr.utils.StringHandle;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class Job2_Top100Reduce extends Reducer<Text, Text, NullWritable, Text> {
    private DecimalFormat df = null;
    private MultipleOutputs<NullWritable, Text> mu = null;
    private static final Text mvalue = new Text();

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        df = new DecimalFormat("#.##");
        mu = new MultipleOutputs(context);
    }

    @Override
    protected void reduce(Text arg0, Iterable<Text> arg1, Context arg2) throws IOException, InterruptedException {
        Iterator<Text> it = arg1.iterator();
        Set<Album_PL> top100_pl_set = new HashSet<>();
        Set<Album_ZB> top100_zb_set = new HashSet<>();
        while (it.hasNext()) {
            Text line = it.next();
            List<String> messages = StringHandle.str_token_split(line.toString(), Constant.SEPARATOR);
            String album_name = messages.get(0);
            String album_id = messages.get(1);
            int play_length = Integer.parseInt(messages.get(2));
            double mac_num = Double.parseDouble(messages.get(3));
            int time_length = Integer.parseInt(messages.get(4));
            //实例化Album_PL
            Album_PL ap = new Album_PL(album_name, album_id, play_length, time_length);
            //实例化Album_ZB
            double zb = ((double) play_length) / mac_num;
            Album_ZB az = new Album_ZB(album_name, album_id, zb, time_length);
            //取各自set的top100
            pushPL(top100_pl_set, ap);
            pushScale(top100_zb_set, az);
        }
        //循环迭代结束
        String partner_channel = arg0.toString();
        //pl:set转list并排序
        List<Album_PL> top100_pl_list = new ArrayList<>();
        top100_pl_list.addAll(top100_pl_set);
        Collections.sort(top100_pl_list);
        //zb:set转list并排序
        List<Album_ZB> top100_zb_list = new ArrayList<>();
        top100_zb_list.addAll(top100_zb_set);
        Collections.sort(top100_zb_list);
        //迭代输出
        for (Album_PL item : top100_pl_list) {
            String album = item.getAlbum();
            String album_id = item.getAlbum_id();
            int play_length = item.getPlay_length();
            int time_length = item.getTime_length();
            String value = StringHandle.str_join(partner_channel, album, album_id, String.valueOf(play_length), String.valueOf(time_length));
            mvalue.set(value);
            mu.write(NullWritable.get(), mvalue, "pl");
        }
        for (Album_ZB item : top100_zb_list) {
            String album = item.getAlbum();
            String album_id = item.getAlbum_id();
            double zb = item.getZb();
            int time_length = item.getTime_length();
            String value = StringHandle.str_join(partner_channel, album, album_id, df.format(zb), String.valueOf(time_length));
            mvalue.set(value);
            mu.write(NullWritable.get(), mvalue, "zb");
        }
    }

    private void pushPL(Set<Album_PL> top100_pl, Album_PL element) {
        if (top100_pl.size() < 100) {
            top100_pl.add(element);
            return;
        }
        Album_PL min = Collections.min(top100_pl);
        if (element.getPlay_length() > min.getPlay_length()) {
            top100_pl.remove(min);
            top100_pl.add(element);
        }
    }


    private void pushScale(Set<Album_ZB> top100_scale, Album_ZB element) {
        if (top100_scale.size() < 100) {
            top100_scale.add(element);
            return;
        }
        Album_ZB min = Collections.min(top100_scale);
        if (element.getZb() > min.getZb()) {
            top100_scale.remove(min);
            top100_scale.add(element);
        }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        mu.close();
    }
}

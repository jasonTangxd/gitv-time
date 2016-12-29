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

public class Job2Top100Reduce extends Reducer<Text, Text, NullWritable, Text> {
    private DecimalFormat df = null;
    private MultipleOutputs<NullWritable, Text> mu = null;
    private static final Text mValue = new Text();

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        df = new DecimalFormat("#.##");
        mu = new MultipleOutputs(context);
    }

    @Override
    protected void reduce(Text arg0, Iterable<Text> arg1, Context arg2) throws IOException, InterruptedException {
        Iterator<Text> it = arg1.iterator();
        Set<Album_PL> top100PlSet = new HashSet<>();
        Set<Album_ZB> top100ZbSet = new HashSet<>();
        while (it.hasNext()) {
            Text line = it.next();
            List<String> messages = StringHandle.str_token_split(line.toString(), Constant.SEPARATOR);
            String albumName = messages.get(0);
            String albumId = messages.get(1);
            long playLength = Long.parseLong(messages.get(2));
            int macNum = Integer.parseInt(messages.get(3));
            int timeLength = Integer.parseInt(messages.get(4));
            Album_PL ap = new Album_PL(albumName, albumId, playLength, timeLength);
            double zb = ((double) playLength) / macNum;
            Album_ZB az = new Album_ZB(albumName, albumId, zb, timeLength);
            //取各自set的top100
            pushPL(top100PlSet, ap);
            pushZb(top100ZbSet, az);
        }
        //循环迭代结束
        String partnerChannel = arg0.toString();
        //pl:set转list并排序
        List<Album_PL> top100PlList = new ArrayList<>();
        top100PlList.addAll(top100PlSet);
        Collections.sort(top100PlList);
        //zb:set转list并排序
        List<Album_ZB> top100ZbList = new ArrayList<>();
        top100ZbList.addAll(top100ZbSet);
        Collections.sort(top100ZbList);

        //迭代输出pl-list
        for (Album_PL item : top100PlList) {
            String album = item.getAlbum();
            String albumId = item.getAlbumId();
            long playLength = item.getPlayLength();
            int timeLength = item.getTimeLength();
            String value = StringHandle.str_join(partnerChannel, album, albumId, String.valueOf(playLength), String.valueOf(timeLength));
            mValue.set(value);
            mu.write(NullWritable.get(), mValue, "pl");
        }
        //迭代输出zb-list
        for (Album_ZB item : top100ZbList) {
            String album = item.getAlbum();
            String albumId = item.getAlbumId();
            double zb = item.getZb();
            int timeLength = item.getTimeLength();
            String value = StringHandle.str_join(partnerChannel, album, albumId, df.format(zb), String.valueOf(timeLength));
            mValue.set(value);
            mu.write(NullWritable.get(), mValue, "zb");
        }
    }

    //pl-set
    private void pushPL(Set<Album_PL> top100PlSet, Album_PL element) {
        if (top100PlSet.size() < 100) {
            top100PlSet.add(element);
            return;
        }
        Album_PL min = Collections.min(top100PlSet);
        if (element.getPlayLength() > min.getPlayLength()) {
            top100PlSet.remove(min);
            top100PlSet.add(element);
        }
    }

    //zb-set
    private void pushZb(Set<Album_ZB> top100ZbSet, Album_ZB element) {
        if (top100ZbSet.size() < 100) {
            top100ZbSet.add(element);
            return;
        }
        Album_ZB min = Collections.min(top100ZbSet);
        if (element.getZb() > min.getZb()) {
            top100ZbSet.remove(min);
            top100ZbSet.add(element);
        }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        mu.close();
    }
}

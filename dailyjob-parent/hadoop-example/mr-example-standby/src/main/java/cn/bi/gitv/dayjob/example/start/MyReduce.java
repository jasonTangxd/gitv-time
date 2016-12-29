package cn.bi.gitv.dayjob.example.start;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

public class MyReduce extends Reducer<Text, Text, Text, Text> {
    private static final Text mvalue = new Text();
    private static final Text mkey = new Text();

    @Override
    protected void reduce(Text arg0, Iterable<Text> arg1, Context arg2) throws IOException, InterruptedException {
        Iterator<Text> it = arg1.iterator();
        int count = 0;
        while (it.hasNext()) {
            count++;
        }
        mkey.set(arg0.toString());
        mvalue.set(count + "");
        arg2.write(mkey, mvalue);
    }

}

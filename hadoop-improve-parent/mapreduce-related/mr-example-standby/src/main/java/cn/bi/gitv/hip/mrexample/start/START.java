package cn.bi.gitv.hip.mrexample.start;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.net.URI;

/**
 * Created by Kang on 2016/12/15.
 */
public class START {
    private static final int VOD_MINSIZE = 1024 * 1024 * 128 * 40;//设置minSize 4*128M

    public static void main(String args[]) throws Exception {
        Configuration conf = new Configuration();
        String input = "/data/log_cleaned/vod_cleaned/2016/12/11/part-m-00012";
        String output = "/ys/demo";
        /*如果结果路径已经存在则删除*/
        deleteOldPath(output, conf);
        Job job1 = Job.getInstance(conf, "no_reduce_test");
        job1.setJarByClass(START.class);
        /*设置Map和Reduce处理类*/
        job1.setMapperClass(MyMap.class);
        job1.setReducerClass(MyReduce.class);

        /*Math.max(minSize, Math.min(maxSize, blockSize))*/
        FileInputFormat.setMinInputSplitSize(job1, VOD_MINSIZE);
        FileInputFormat.setMaxInputSplitSize(job1, 10000);//设置maxSize

        /* 设置map输出类型*/
        job1.setMapOutputKeyClass(Text.class);
        job1.setMapOutputValueClass(Text.class);
        /* 设置总的输出类型*/
        job1.setOutputKeyClass(Text.class);
        job1.setOutputValueClass(Text.class);
        /* 设置输入输出文件格式*/
        job1.setInputFormatClass(TextInputFormat.class);//TextInputFormat extends FileInputFormat
        job1.setOutputFormatClass(TextOutputFormat.class);
        /* 配置输入输出信息*/
        FileInputFormat.setInputPaths(job1, input);
        FileOutputFormat.setOutputPath(job1, new Path(output));
        job1.setNumReduceTasks(20);
        System.exit(job1.waitForCompletion(true) ? 0 : 1);
    }

    /*删除已有输出路径*/
    private static void deleteOldPath(String path, Configuration conf) throws Exception {
        final FileSystem fileSystem = FileSystem.get(new URI(path), conf);
        final Path outPath = new Path(path);
        if (fileSystem.exists(outPath)) {
            fileSystem.delete(outPath, true);
        }
    }
}

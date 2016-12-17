package cn.gitv.bi.viscosity.cdhmr.start;

import cn.gitv.bi.viscosity.cdhmr.constant.Constant;
import cn.gitv.bi.viscosity.cdhmr.mrimpl.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.net.URI;

public class Start_up {
    private static final int VOD_MINSIZE = 1024 * 1024 * 128 * 40;//设置minSize 4*128M
    private static final int LIV_MINSIZE = 1024 * 1024 * 128 * 8;

    public static void main(String[] args) throws Exception {
        //hadoop jar vom.jar [vod] [days] [today]
        if (args.length != 3) {
            throw new IllegalArgumentException("args is error!");
        }
        String type = args[0];
        String daysPath = args[1];
        String today = args[2];
        String job1Input = null;
        String job1Output = null;
        String job2Output = null;
        /*vod 和liv 采用相似的计算模型,在第一个map的时候稍有不同,且输入、输出1、输出2路径不同*/
        if ("vod".equals(type)) {
            job1Input = getVodInputPath(daysPath);
            job1Output = Constant.VOD_RESULT1_PATH;
            job2Output = String.format(Constant.RESULT2_PATH, today, type);
        } else if ("liv".equals(type)) {
            job1Input = getLivInputPath(daysPath);
            job1Output = Constant.LIV_RESULT1_PATH;
            job2Output = String.format(Constant.RESULT2_PATH, today, type);
        } else {
            System.out.println("enumtype is not in [vod,liv],removed path and try again");
            System.exit(0);
        }
        submitJob1(job1Input, job1Output, type);
        submitJob2(job1Output, job2Output, type);
    }


    private static void submitJob1(String input, String output, String type) throws Exception {
        Configuration conf = new Configuration();
        /*如果结果路径已经存在则删除*/
        deleteOldPath(output, conf);
        Job job1 = Job.getInstance(conf, "vis_group_pl_sum_" + type);
        job1.setJarByClass(Start_up.class);
        /*设置Map和Reduce处理类*/
        if ("vod".equals(type)) {
            job1.setMapperClass(Job1_VOD_FilterMap.class);
            /*Math.max(minSize, Math.min(maxSize, blockSize))*/
            FileInputFormat.setMinInputSplitSize(job1, VOD_MINSIZE);
            FileInputFormat.setMaxInputSplitSize(job1, 10000);//设置maxSize
        } else if ("liv".equals(type)) {
            job1.setMapperClass(Job1_LIV_FilterMap.class);
            FileInputFormat.setMinInputSplitSize(job1, LIV_MINSIZE);//设置minSize
            FileInputFormat.setMaxInputSplitSize(job1, 10000);//设置maxSize
        }
        job1.setReducerClass(Job1_PLReduce.class);
        /* 设置map输出类型*/
        job1.setMapOutputKeyClass(Text.class);
        job1.setMapOutputValueClass(Text.class);
        /* 设置总的输出类型*/
        job1.setOutputKeyClass(NullWritable.class);
        job1.setOutputValueClass(Text.class);
        /* 设置输入输出文件格式*/
        job1.setInputFormatClass(SequenceFileInputFormat.class);
        job1.setOutputFormatClass(TextOutputFormat.class);
        /* 配置输入输出信息*/
        FileInputFormat.setInputPaths(job1, input);
        FileOutputFormat.setOutputPath(job1, new Path(output));
        job1.setNumReduceTasks(20);
        job1.waitForCompletion(true);
    }

    private static void submitJob2(String input, String output, String type) throws Exception {
        Configuration conf = new Configuration();
        /*如果结果路径已经存在则删除*/
        deleteOldPath(output, conf);
        Job job2 = Job.getInstance(conf, "vis_final_album_top100_" + type);
        job2.setJarByClass(Start_up.class);
        /*设置Map和Reduce处理类*/
        job2.setMapperClass(Job2_ChannelMap.class);
        job2.setReducerClass(Job2_Top100Reduce.class);
        /*设置map输出类型*/
        job2.setMapOutputKeyClass(Text.class);
        job2.setMapOutputValueClass(Text.class);
        /*设置总的输出类型*/
        job2.setOutputKeyClass(NullWritable.class);
        job2.setOutputValueClass(Text.class);
        /*设置输入输出文件格式*/
        job2.setOutputFormatClass(TextOutputFormat.class);
        /*配置输入输出信息*/
        FileInputFormat.setInputPaths(job2, new Path(input));
        FileOutputFormat.setOutputPath(job2, new Path(output));
        //因为全局排序top100,所以归为1个reduce
        job2.setNumReduceTasks(1);
//        job2.submit();
        System.exit(job2.waitForCompletion(true) ? 0 : 1);
    }

    /*返回点播的输入路径*/
    private static String getVodInputPath(String days) {
        String[] need_day = days.split(" ");
        StringBuilder paths = new StringBuilder();
        for (String day : need_day) {
            String path = String.format(Constant.VOD_DATA_PATH, day);
            paths.append(path).append("/*").append(",");
        }
        return paths.deleteCharAt(paths.length() - 1).toString();
    }

    /*返回直播的输入路径*/
    private static String getLivInputPath(String days) {
        String[] need_day = days.split(" ");
        StringBuilder paths = new StringBuilder();
        for (String day : need_day) {
            String path = String.format(Constant.LIV_DATA_PATH, day);
            paths.append(path).append("/*").append(",");
        }
        return paths.deleteCharAt(paths.length() - 1).toString();
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

package cn.bi.gitv.hip.parquetdemo.start;

import cn.bi.gitv.hip.parquetdemo.mrimp.MyMapper;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.parquet.hadoop.ParquetFileReader;
import org.apache.parquet.hadoop.ParquetInputFormat;
import org.apache.parquet.hadoop.ParquetOutputFormat;
import org.apache.parquet.hadoop.example.GroupWriteSupport;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.apache.parquet.hadoop.metadata.ParquetMetadata;
import org.apache.parquet.schema.MessageType;

import java.net.URI;

public class START {
    private static final int VOD_MINSIZE = 1024 * 1024 * 128 * 40;//设置minSize 4*128M

    public static void main(String args[]) throws Exception {
        String input = "/user/hive/warehouse/intelligence.db/parquet_gitv_log_original/logdate=2016-12-01/partner=AH_CMCC/";
        String output = "/ys/demo";
        String parquetSchemaPath = "/ys/schema/demo.parq";
        String compression = (args.length > 0) ? args[0] : "none";
        Configuration conf = new Configuration();
        //如果结果路径已经存在则删除
        deleteOldPath(output, conf);
        /*加载parquet的schema*/
        ParquetMetadata readFooter = ParquetFileReader.readFooter(conf, new Path(parquetSchemaPath));
        MessageType schema = readFooter.getFileMetaData().getSchema();
        GroupWriteSupport.setSchema(schema, conf);

        Job job1 = Job.getInstance(conf, "parquet_demo");
        job1.setJarByClass(START.class);
        /*设置Map和Reduce处理类*/
        job1.setMapperClass(MyMapper.class);
//        job1.setReducerClass(MyReduce.class);
        /*Math.max(minSize, Math.min(maxSize, blockSize))*/
//        ParquetInputFormat.setMinInputSplitSize(job1, VOD_MINSIZE);
//        ParquetInputFormat.setMaxInputSplitSize(job1, 10000);//设置maxSize

        /* 设置map输出类型*/
        job1.setMapOutputKeyClass(Text.class);
        job1.setMapOutputValueClass(Text.class);
        /* 设置总的输出类型*/
//        job1.setOutputKeyClass(Text.class);
//        job1.setOutputValueClass(Text.class);
        job1.setNumReduceTasks(0);
        /* 设置输入输出文件格式*/
        job1.setInputFormatClass(ParquetInputFormat.class);//TextInputFormat extends FileInputFormat
        job1.setOutputFormatClass(ParquetOutputFormat.class);
        /* 配置输入输出信息*/
        CompressionCodecName codec = CompressionCodecName.UNCOMPRESSED;
        if (compression.equalsIgnoreCase("snappy")) {
            codec = CompressionCodecName.SNAPPY;
        } else if (compression.equalsIgnoreCase("gzip")) {
            codec = CompressionCodecName.GZIP;
        }
        ParquetInputFormat.setInputPaths(job1, input);
        ParquetOutputFormat.setOutputPath(job1, new Path(output));
        ParquetOutputFormat.setCompression(job1, codec);
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

package cn.gitv.bi.external.khloader.start;

import cn.gitv.bi.external.khloader.custom.MyMapper;
import cn.gitv.bi.external.khloader.overwrite.KafkaInputFormat;
import cn.gitv.bi.external.khloader.overwrite.MultiOutputFormat;
import cn.gitv.bi.external.khloader.utils.CheckpointManager;
import cn.gitv.bi.external.khloader.utils.CmdOption;
import org.apache.commons.cli.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartJob {
    private final static Logger LOG = LoggerFactory.getLogger(StartJob.class);

    public static void main(String args[]) throws Exception {
        CommandLineParser parser = new PosixParser();
        Options options = CmdOption.buildOptions();
        CommandLine cmd = parser.parse(options, args);
        /*如果参数中包含h代表help或者参数长度为0,则显示相应帮助消息*/
        if (cmd.hasOption("h") || cmd.getArgs().length == 0) {
            printHelpAndExit(options);
        }
        Configuration conf = new Configuration();
        conf.setBoolean("mapred.map.tasks.speculative.execution", false);
        String hdfsPath = cmd.getArgs()[0];
        if (cmd.hasOption("topics")) {
            LOG.info("Using topics: " + cmd.getOptionValue("topics"));
            KafkaInputFormat.configureKafkaTopics(conf, cmd.getOptionValue("topics"));
        } else {
            printHelpAndExit(options);
        }
        KafkaInputFormat.configureZkConnection(conf, cmd.getOptionValue("zk-connect", "localhost:2181"));
        if (cmd.hasOption("consumer-group")) {
            CheckpointManager.configureUseZooKeeper(conf, cmd.getOptionValue("consumer-group", "dev-hadoop-loader"));
        }
        if (cmd.getOptionValue("autooffset-reset") != null) {
            KafkaInputFormat.configureAutoOffsetReset(conf, cmd.getOptionValue("autooffset-reset"));
        }
        Job job = Job.getInstance(conf, "kafka.hadoop.loader");
        job.setJarByClass(StartJob.class);
        job.setInputFormatClass(KafkaInputFormat.class);
        job.setMapperClass(MyMapper.class);
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);
        job.setOutputFormatClass(MultiOutputFormat.class);
        job.setNumReduceTasks(0);
        MultiOutputFormat.setOutputPath(job, new Path(hdfsPath));
        MultiOutputFormat.configurePathFormat(conf, "'{T}/'yyyy-MM-dd'/'");
        MultiOutputFormat.setCompressOutput(job, cmd.getOptionValue("compress-output", "on").equals("on"));
        LOG.info("Output hdfs location: {}", hdfsPath);
//        LOG.info("Output hdfs compression: {}", MultiOutputFormat.getCompressOutput(job));
        job.submit();
//        System.exit(job.waitForCompletion(true) ? 0 : -1);
    }

    private static void printHelpAndExit(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("kafka-hadoop-loader.jar", options);
        System.exit(0);
    }
}

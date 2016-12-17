package cn.gitv.bi.launcher.tohdfs.start;

import cn.gitv.bi.external.storm2hdfs.bolt.SequenceFileBolt;
import cn.gitv.bi.external.storm2hdfs.bolt.format.DefaultFileNameFormat;
import cn.gitv.bi.external.storm2hdfs.bolt.format.DefaultSequenceFormat;
import cn.gitv.bi.external.storm2hdfs.bolt.format.FileNameFormat;
import cn.gitv.bi.external.storm2hdfs.bolt.rotation.FileRotationPolicy;
import cn.gitv.bi.external.storm2hdfs.bolt.rotation.FileSizeRotationPolicy;
import cn.gitv.bi.external.storm2hdfs.bolt.rotation.TimedClosePolicy;
import cn.gitv.bi.external.storm2hdfs.bolt.rotation.TtlPolicy;
import cn.gitv.bi.external.storm2hdfs.bolt.sync.CountSyncPolicy;
import cn.gitv.bi.external.storm2hdfs.bolt.sync.SyncPolicy;
import cn.gitv.bi.launcher.tohdfs.bolt.PartitionBolt;
import cn.gitv.bi.launcher.tohdfs.myimp.hdfs.ActionLogDatePartition;
import org.apache.hadoop.io.SequenceFile;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.kafka.spout.KafkaSpoutStreams;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.utils.Utils;

import static cn.gitv.bi.launcher.tohdfs.myimp.kafka.KafkaBoltUtil.*;
import static org.apache.storm.kafka.spout.KafkaSpoutConfig.FirstPollOffsetStrategy.EARLIEST;

/**
 * Created by Kang on 2016/11/30.
 */
public class Start_up {

    ///set kafka spout
    public static KafkaSpoutConfig<String, String> newKafkaSpoutConfig(KafkaSpoutStreams kafkaSpoutStreams) {
        return new KafkaSpoutConfig.Builder<String, String>(newKafkaConsumerProps(), kafkaSpoutStreams, newTuplesBuilder(), newRetryService())
                .setOffsetCommitPeriodMs(10_000).setFirstPollOffsetStrategy(EARLIEST).setMaxUncommittedOffsets(250).build();
    }

    private static SequenceFileBolt getHdfsBolt() {
        //set hdfs bolt
        SyncPolicy syncPolicy = new CountSyncPolicy(1000);
        FileRotationPolicy rotationPolicy = new FileSizeRotationPolicy(256.0f, FileSizeRotationPolicy.Units.MB);
        TtlPolicy ttlPolicy = new TimedClosePolicy(5.0f, 20.0f, TimedClosePolicy.TimeUnit.MINUTES);
        ActionLogDatePartition actionLogDatePartition = new ActionLogDatePartition();
        FileNameFormat fileNameFormat = new DefaultFileNameFormat().withExtension(".seq").withPath("/launcher/");
        DefaultSequenceFormat format = new DefaultSequenceFormat("TS", "Content");
        SequenceFileBolt seqbolt = new SequenceFileBolt().withFsUrl("hdfs://ns").withFileNameFormat(fileNameFormat).withSequenceFormat(format).withPartitioner(actionLogDatePartition)
                .withRotationPolicy(rotationPolicy).withTtlPolicy(ttlPolicy).withSyncPolicy(syncPolicy).withCompressionType(SequenceFile.CompressionType.RECORD).withCompressionCodec("deflate");
        return seqbolt;
    }

    public static void main(String[] args) {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("launcher_clean", new KafkaSpout<>(newKafkaSpoutConfig(newKafkaSpoutStreams())), 3);
        builder.setBolt("partition_bolt", new PartitionBolt(), 3).shuffleGrouping("launcher_clean");
        builder.setBolt("write_to_hdfs", getHdfsBolt(), 3).shuffleGrouping("partition_bolt");
        Config conf = new Config();
        conf.setMaxSpoutPending(10000);
        conf.setMessageTimeoutSecs(60);
        if (args == null || args.length == 0) {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("topo", conf, builder.createTopology());
            Utils.sleep(100000);
            cluster.killTopology("topo");
            cluster.shutdown();
        } else {
            conf.setNumWorkers(3);
            try {
                StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}

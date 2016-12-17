package cn.gitv.bi.external.storm2hdfs.someimp;

import org.apache.hadoop.io.SequenceFile;
import cn.gitv.bi.external.storm2hdfs.bolt.SequenceFileBolt;
import cn.gitv.bi.external.storm2hdfs.bolt.format.DefaultFileNameFormat;
import cn.gitv.bi.external.storm2hdfs.bolt.format.FileNameFormat;
import cn.gitv.bi.external.storm2hdfs.bolt.format.SequenceFormat;
import cn.gitv.bi.external.storm2hdfs.bolt.rotation.FileRotationPolicy;
import cn.gitv.bi.external.storm2hdfs.bolt.rotation.FileSizeRotationPolicy;
import cn.gitv.bi.external.storm2hdfs.bolt.rotation.TimedClosePolicy;
import cn.gitv.bi.external.storm2hdfs.bolt.rotation.TtlPolicy;
import cn.gitv.bi.external.storm2hdfs.bolt.sync.CountSyncPolicy;
import cn.gitv.bi.external.storm2hdfs.bolt.sync.SyncPolicy;

/**
 * Created by Kang on 2016/12/2.
 */
public class Example {
    public void howToUse() {
        SyncPolicy syncPolicy = new CountSyncPolicy(10000);//数量同步策略
        FileRotationPolicy rotationPolicy = new FileSizeRotationPolicy(256.0f, FileSizeRotationPolicy.Units.MB);//文件大小切分策略
        TtlPolicy ttlPolicy = new TimedClosePolicy(5.0f, 20.0f, TimedClosePolicy.TimeUnit.MINUTES);//ttl连接时长策略
        ActionLogDatePartition actionLogDatePartition = new ActionLogDatePartition();
        FileNameFormat fileNameFormat = new DefaultFileNameFormat().withExtension(".seq").withPath("/launcher/");
        SequenceFormat format = new NoKeySequenceFormat("Content");
        SequenceFileBolt seqbolt = new SequenceFileBolt().withFsUrl("hdfs://ns").withFileNameFormat(fileNameFormat).withSequenceFormat(format).withPartitioner(actionLogDatePartition)
                .withRotationPolicy(rotationPolicy).withTtlPolicy(ttlPolicy).withSyncPolicy(syncPolicy).withCompressionType(SequenceFile.CompressionType.RECORD).withCompressionCodec("deflate");
        //then you use this seqbolt for sth;
    }
}

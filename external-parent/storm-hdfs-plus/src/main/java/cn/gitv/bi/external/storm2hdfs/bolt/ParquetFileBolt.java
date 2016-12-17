/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.gitv.bi.external.storm2hdfs.bolt;

import cn.gitv.bi.external.storm2hdfs.bolt.format.FileNameFormat;
import cn.gitv.bi.external.storm2hdfs.bolt.format.SequenceFormat;
import cn.gitv.bi.external.storm2hdfs.bolt.rotation.FileRotationPolicy;
import cn.gitv.bi.external.storm2hdfs.bolt.rotation.TtlPolicy;
import cn.gitv.bi.external.storm2hdfs.bolt.sync.SyncPolicy;
import cn.gitv.bi.external.storm2hdfs.common.AbstractHDFSWriter;
import cn.gitv.bi.external.storm2hdfs.common.Partitioner;
import cn.gitv.bi.external.storm2hdfs.common.SequenceFileWriter;
import cn.gitv.bi.external.storm2hdfs.common.rotation.RotationAction;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

public class ParquetFileBolt extends AbstractHdfsBolt {
    private static final Logger LOG = LoggerFactory.getLogger(ParquetFileBolt.class);

    private SequenceFormat format;
    private SequenceFile.CompressionType compressionType = SequenceFile.CompressionType.RECORD;//不采用压缩/记录级别的压缩/块级别的压缩
    //transient型变量的值不包括在序列化
    private transient SequenceFile.Writer writer;

    private String compressionCodec = "default";
    private transient CompressionCodecFactory codecFactory;

    public ParquetFileBolt() {
    }

    public ParquetFileBolt withCompressionCodec(String codec) {
        //设置压缩编码格式
        this.compressionCodec = codec;
        return this;
    }

    public ParquetFileBolt withFsUrl(String fsUrl) {
        //设置文件系统url
        this.fsUrl = fsUrl;
        return this;
    }

    public ParquetFileBolt withConfigKey(String configKey) {
        this.configKey = configKey;
        return this;
    }

    public ParquetFileBolt withFileNameFormat(FileNameFormat fileNameFormat) {
        this.fileNameFormat = fileNameFormat;
        return this;
    }

    public ParquetFileBolt withSequenceFormat(SequenceFormat format) {
        this.format = format;
        return this;
    }

    public ParquetFileBolt withSyncPolicy(SyncPolicy syncPolicy) {
        this.syncPolicy = syncPolicy;
        return this;
    }

    public ParquetFileBolt withRotationPolicy(FileRotationPolicy rotationPolicy) {
        this.rotationPolicy = rotationPolicy;
        return this;
    }

    public ParquetFileBolt withTtlPolicy(TtlPolicy ttlPolicy) {
        this.ttlPolicy = ttlPolicy;
        return this;
    }

    public ParquetFileBolt withCompressionType(SequenceFile.CompressionType compressionType) {
        this.compressionType = compressionType;
        return this;
    }

    public ParquetFileBolt withTickTupleIntervalSeconds(int interval) {
        this.tickTupleInterval = interval;
        return this;
    }

    public ParquetFileBolt addRotationAction(RotationAction action) {
        this.rotationActions.add(action);
        return this;
    }

    public ParquetFileBolt withRetryCount(int fileRetryCount) {
        this.fileRetryCount = fileRetryCount;
        return this;
    }

    public ParquetFileBolt withPartitioner(Partitioner partitioner) {
        this.partitioner = partitioner;
        return this;
    }

    public ParquetFileBolt withMaxOpenFiles(int maxOpenFiles) {
        //最大连接文件数,默认值是50个writer
        this.maxOpenFiles = maxOpenFiles;
        return this;
    }

    @Override
    public void doPrepare(Map conf, TopologyContext topologyContext, OutputCollector collector) throws IOException {
        LOG.info("Preparing Sequence File Bolt...");
        if (this.format == null) throw new IllegalStateException("SequenceFormat must be specified.");

        this.fs = FileSystem.get(URI.create(this.fsUrl), hdfsConfig);//获得fs实例
        this.codecFactory = new CompressionCodecFactory(hdfsConfig);
    }

    @Override
    protected String getWriterKey(Tuple tuple) {
        return "PARQUET";
    }

    @Override
    protected AbstractHDFSWriter makeNewWriter(Path path, Tuple tuple) throws IOException {
        //实例化SequenceFile.Writer
        SequenceFile.Writer writer = SequenceFile.createWriter(this.hdfsConfig, SequenceFile.Writer.file(path),
                SequenceFile.Writer.keyClass(this.format.keyClass()), SequenceFile.Writer.valueClass(this.format.valueClass()),
                SequenceFile.Writer.compression(this.compressionType, this.codecFactory.getCodecByName(this.compressionCodec))
        );
        return new SequenceFileWriter(this.rotationPolicy, path, writer, this.format);
    }
}

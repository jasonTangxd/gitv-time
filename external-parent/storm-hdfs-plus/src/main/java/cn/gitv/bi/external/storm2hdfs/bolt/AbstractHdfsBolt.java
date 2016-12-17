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
import cn.gitv.bi.external.storm2hdfs.bolt.rotation.FileRotationPolicy;
import cn.gitv.bi.external.storm2hdfs.bolt.rotation.TimedClosePolicy;
import cn.gitv.bi.external.storm2hdfs.bolt.rotation.TtlPolicy;
import cn.gitv.bi.external.storm2hdfs.bolt.sync.SyncPolicy;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import cn.gitv.bi.external.storm2hdfs.common.AbstractHDFSWriter;
import cn.gitv.bi.external.storm2hdfs.common.NullPartitioner;
import cn.gitv.bi.external.storm2hdfs.common.Partitioner;
import cn.gitv.bi.external.storm2hdfs.common.rotation.RotationAction;
import cn.gitv.bi.external.storm2hdfs.common.security.HdfsSecurityUtil;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.utils.TupleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * Created by Kang on 2016/11/30.
 */
public abstract class AbstractHdfsBolt extends BaseRichBolt {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractHdfsBolt.class);
    private static final Integer DEFAULT_RETRY_COUNT = 3;
    /**
     * Half of the default Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS
     */
    private static final int DEFAULT_TICK_TUPLE_INTERVAL_SECS = 15;
    private static final Integer DEFAULT_MAX_OPEN_FILES = 50;

    protected Map<String, AbstractHDFSWriter> writers;
    protected Map<String, Long> writersTtl;
    //保持writers和writersTtl同时操作
    protected Map<String, Integer> rotationCounterMap = new HashMap<>();
    protected List<RotationAction> rotationActions = new ArrayList<>();
    protected OutputCollector collector;
    protected transient FileSystem fs;
    protected SyncPolicy syncPolicy;
    protected FileRotationPolicy rotationPolicy;
    protected TtlPolicy ttlPolicy;
    protected FileNameFormat fileNameFormat;
    protected String fsUrl;
    protected String configKey;
    protected transient Object writeLock;
    protected transient Timer rotationTimer; // only used for TimedRotationPolicy
    private List<Tuple> tupleBatch = new LinkedList<>();
    protected long offset = 0;
    protected Integer fileRetryCount = DEFAULT_RETRY_COUNT;
    protected Integer tickTupleInterval = DEFAULT_TICK_TUPLE_INTERVAL_SECS;
    protected Integer maxOpenFiles = DEFAULT_MAX_OPEN_FILES;
    protected Partitioner partitioner = new NullPartitioner();
    protected transient Configuration hdfsConfig; //hdfs config

    /**
     * Marked as final to prevent override. Subclasses should implement the doPrepare() method.
     *
     * @param conf
     * @param topologyContext
     * @param collector
     */
    public final void prepare(Map conf, TopologyContext topologyContext, OutputCollector collector) {
        this.writeLock = new Object();
        if (this.syncPolicy == null) throw new IllegalStateException("SyncPolicy must be specified.");
        if (this.rotationPolicy == null) throw new IllegalStateException("RotationPolicy must be specified.");
        if (this.ttlPolicy == null) throw new IllegalStateException("ttlPolicy must be specified.");
        if (this.fsUrl == null) {
            throw new IllegalStateException("File system URL must be specified.");
        }

        writers = new WritersMap(this.maxOpenFiles);
        writersTtl = new LinkedHashMap(this.maxOpenFiles);

        this.collector = collector;
        this.fileNameFormat.prepare(conf, topologyContext);
        this.hdfsConfig = new Configuration();
        Map<String, Object> map = (Map<String, Object>) conf.get(this.configKey);
        if (map != null) {
            for (String key : map.keySet()) {
                this.hdfsConfig.set(key, String.valueOf(map.get(key)));
            }
        }

        try {
            HdfsSecurityUtil.login(conf, hdfsConfig);
            doPrepare(conf, topologyContext, collector);
        } catch (Exception e) {
            throw new RuntimeException("Error preparing HdfsBolt: " + e.getMessage(), e);
        }

//        if (this.rotationPolicy instanceof TimedRotationPolicy)
        if (ttlPolicy != null)
            startTimedPolicy();
    }

    @Override
    public final void execute(Tuple tuple) {

        synchronized (this.writeLock) {
            boolean forceSync = false;
            AbstractHDFSWriter writer = null;
            String writerKey = null;

            if (TupleUtils.isTick(tuple)) {
                LOG.debug("TICK! forcing a file system flush");
                this.collector.ack(tuple);
                forceSync = true;//系统时间触发强制同步策略
            } else {

                writerKey = getHashKeyForTuple(tuple);
                //Constant + "****" + A=101/2016-11-20
                try {
                    writer = getOrCreateWriter(writerKey, tuple);
                    this.offset = writer.write(tuple);
                    tupleBatch.add(tuple);
                } catch (IOException e) {
                    //If the write failed, try to sync anything already written
                    LOG.info("Tuple failed to write, forcing a flush of existing data.");
                    this.collector.reportError(e);
                    forceSync = true;
                    this.collector.fail(tuple);
                }
            }
            //同步策略以及强制同步策略触发
            if (this.syncPolicy.mark(tuple, this.offset) || (forceSync && tupleBatch.size() > 0)) {
                int attempts = 0;
                boolean success = false;
                IOException lastException = null;
                // Make every attempt to sync the data we have.  If it can't be done then kill the bolt with
                // a runtime exception.  The filesystem is presumably in a very bad state.
                while (success == false && attempts < fileRetryCount) {
                    attempts += 1;
                    try {
                        //把所有数据刷入磁盘flush()
                        syncAllWriters();
                        LOG.debug("Data synced to filesystem. Ack'ing [{}] tuples", tupleBatch.size());
                        for (Tuple t : tupleBatch) {
                            this.collector.ack(t);
                        }
                        tupleBatch.clear();
                        syncPolicy.reset();
                        success = true;
                    } catch (IOException e) {
                        LOG.warn("Data could not be synced to filesystem on attempt [{}]", attempts);
                        this.collector.reportError(e);
                        lastException = e;
                    }
                }

                // If unsuccesful fail the pending tuples
                if (success == false) {
                    LOG.warn("Data could not be synced to filesystem, failing this batch of tuples");
                    for (Tuple t : tupleBatch) {
                        this.collector.fail(t);
                    }
                    tupleBatch.clear();

                    throw new RuntimeException("Sync failed [" + attempts + "] times.", lastException);
                }
            }

            if (writer != null && writer.needsRotation()) {
                //触发切换策略
                doRotationAndRemoveWriter(writerKey, writer);
            }
        }
    }

    private AbstractHDFSWriter getOrCreateWriter(String writerKey, Tuple tuple) throws IOException {
        AbstractHDFSWriter writer;
        //从map中判断是否含有
        writer = writers.get(writerKey);
        if (writer == null) {
            Path pathForNextFile = getBasePathForNextFile(tuple);//hdfs://ns/launcher/A=101/2016-11-20/xxx-xxx-xxx.seq
            writer = makeNewWriter(pathForNextFile, tuple);
            writers.put(writerKey, writer);
            //创建writers的时候记录开始时间
            writersTtl.put(writerKey, System.currentTimeMillis());
        }
        return writer;
    }

    /**
     * A tuple must be mapped to a writer based on two factors:
     * - bolt specific logic that must separate tuples into different files in the same directory (see the avro bolt
     * for an example of this)
     * - the directory the tuple will be partioned into
     *
     * @param tuple
     * @return
     */
    private String getHashKeyForTuple(Tuple tuple) {
        final String boltKey = getWriterKey(tuple);
        final String partitionDir = this.partitioner.getPartitionPath(tuple);
        return boltKey + "****" + partitionDir;
    }

    protected Path getBasePathForNextFile(Tuple tuple) {

        final String partitionPath = this.partitioner.getPartitionPath(tuple);
        final int rotation;
        if (rotationCounterMap.containsKey(partitionPath)) {
            rotation = rotationCounterMap.get(partitionPath) + 1;
        } else {
            rotation = 0;
        }
        rotationCounterMap.put(partitionPath, rotation);

        //hdfs://ns/launcher/A=101/2016-11-20/xxx-xxx-xxx.seq
        return new Path(this.fsUrl + this.fileNameFormat.getPath() + partitionPath,
                this.fileNameFormat.getName(rotation, System.currentTimeMillis()));
    }


    private void doRotationAndRemoveAllWriters() {
        for (final AbstractHDFSWriter writer : writers.values()) {
            try {
                rotateOutputFile(writer);
            } catch (IOException e) {
                LOG.warn("IOException during scheduled file rotation.", e);
            }
        }
        writers.clear();
        writersTtl.clear();
    }

    void doRotationAndRemoveWriter(String writerKey, AbstractHDFSWriter writer) {
        try {
            rotateOutputFile(writer);//关流
        } catch (IOException e) {
            this.collector.reportError(e);
            LOG.error("File could not be rotated");
            //At this point there is nothing to do.  In all likelihood any filesystem operations will fail.
            //The next tuple will almost certainly fail to write and/or sync, which force a rotation.  That
            //will give rotateAndReset() a chance to work which includes creating a fresh file handle.
        } finally {
            //从集合中移除这个writerKey
            writers.remove(writerKey);
            writersTtl.remove(writerKey);
        }
    }

    protected void rotateOutputFile(AbstractHDFSWriter writer) throws IOException {
        LOG.info("Rotating output file...");
        long start = System.currentTimeMillis();
        synchronized (this.writeLock) {
            writer.close();
//　关流再创建则切换新文件
            LOG.info("Performing {} file rotation actions.", this.rotationActions.size());
            for (RotationAction action : this.rotationActions) {
                action.execute(this.fs, writer.getFilePath());
            }
        }
        long time = System.currentTimeMillis() - start;
        LOG.info("File rotation took {} ms.", time);
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return TupleUtils.putTickFrequencyIntoComponentConfig(super.getComponentConfiguration(), tickTupleInterval);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
    }

    @Override
    public void cleanup() {
        doRotationAndRemoveAllWriters();
    }


    private void syncAllWriters() throws IOException {
        for (AbstractHDFSWriter writer : writers.values()) {
            writer.sync();
        }
    }

    private void startTimedPolicy() {
        long interval = ((TimedClosePolicy) this.ttlPolicy).getInterval();
        final long outTime = ((TimedClosePolicy) this.ttlPolicy).getOutTime();
        this.rotationTimer = new Timer(true);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                doTtlClose(outTime);
            }
        };
        this.rotationTimer.scheduleAtFixedRate(task, interval, interval);
    }

    private void doTtlClose(long outTime) {
        for (Map.Entry<String, Long> item : writersTtl.entrySet()) {
            long startTime = item.getValue();
            long time = System.currentTimeMillis() - startTime;
            try {
                if (time > outTime) {
                    String writeKey = item.getKey();
                    rotateOutputFile(writers.get(writeKey));
                    writers.remove(writeKey);
                    writersTtl.remove(writeKey);
                }
            } catch (IOException e) {
                LOG.warn("IOException during scheduled file rotation.", e);
            }
        }
    }

    abstract protected void doPrepare(Map conf, TopologyContext topologyContext, OutputCollector collector) throws IOException;

    abstract protected String getWriterKey(Tuple tuple);

    abstract protected AbstractHDFSWriter makeNewWriter(Path path, Tuple tuple) throws IOException;

    static class WritersMap extends LinkedHashMap<String, AbstractHDFSWriter> {
        final long maxWriters;

        public WritersMap(long maxWriters) {
            super((int) maxWriters, 0.75f, true);
            this.maxWriters = maxWriters;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<String, AbstractHDFSWriter> eldest) {
            return this.size() > this.maxWriters;
        }
    }
}

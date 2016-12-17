/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.gitv.bi.external.storm2hdfs.bolt.rotation;


import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * File rotation policyimp that will rotate files when a certain
 * file size is reached.
 *
 * For example:
 * <pre>
 *     // rotate when files reach 5MB
 *     FileSizeRotationPolicy policyimp =
 *          new FileSizeRotationPolicy(5.0, Units.MB);
 * </pre>
 *
 */
public class FileSizeRotationPolicy implements FileRotationPolicy {
    private static final Logger LOG = LoggerFactory.getLogger(FileSizeRotationPolicy.class);

    protected FileSizeRotationPolicy(long maxBytes) {
        this.maxBytes = maxBytes;
    }

    public FileSizeRotationPolicy(float count, Units units) {
        this.maxBytes = (long) (count * units.getByteCount());
    }
    public static enum Units {

        KB((long)Math.pow(2, 10)),
        MB((long)Math.pow(2, 20)),
        GB((long)Math.pow(2, 30)),
        TB((long)Math.pow(2, 40));

        private long byteCount;

        private Units(long byteCount){
            this.byteCount = byteCount;
        }

        public long getByteCount(){
            return byteCount;
        }
    }

    private long maxBytes;
    private long lastOffset = 0;
    private long currentBytesWritten = 0;



    @Override
    public boolean mark(Tuple tuple, long offset) {
        long diff = offset - this.lastOffset;
        this.currentBytesWritten += diff;
        this.lastOffset = offset;
        return this.currentBytesWritten >= this.maxBytes;
    }

    @Override
    public void reset() {
        this.currentBytesWritten = 0;
        this.lastOffset = 0;
    }

    @Override
    public FileRotationPolicy copy() {
        return new FileSizeRotationPolicy(this.maxBytes);
    }
}

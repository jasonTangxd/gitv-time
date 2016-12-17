/*
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

public class TimedClosePolicy implements TtlPolicy {

    public static enum TimeUnit {

        SECONDS((long) 1000),
        MINUTES((long) 1000 * 60),
        HOURS((long) 1000 * 60 * 60),
        DAYS((long) 1000 * 60 * 60 * 24);

        private long milliSeconds;

        private TimeUnit(long milliSeconds) {
            this.milliSeconds = milliSeconds;
        }

        public long getMilliSeconds() {
            return milliSeconds;
        }
    }

    private long interval;
    private long outTime;

    public TimedClosePolicy(float count, float count_o, TimeUnit units) {
        this.interval = (long) (count * units.getMilliSeconds());
        this.outTime = (long) (count_o * units.getMilliSeconds());
    }

    protected TimedClosePolicy(long interval) {
        this.interval = interval;
    }


    @Override
    public boolean mark(Tuple tuple, long offset) {
        return false;
    }
    
    @Override
    public void reset() {

    }

    @Override
    public TtlPolicy copy() {
        return new TimedClosePolicy(this.interval);
    }

    public long getInterval() {
        return this.interval;
    }

    public long getOutTime() {
        return this.outTime;
    }
}

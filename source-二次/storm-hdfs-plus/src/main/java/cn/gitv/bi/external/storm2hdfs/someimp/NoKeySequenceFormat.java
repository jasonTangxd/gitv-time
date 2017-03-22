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
package cn.gitv.bi.external.storm2hdfs.someimp;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import cn.gitv.bi.external.storm2hdfs.bolt.format.SequenceFormat;
import org.apache.storm.tuple.Tuple;

/**
 * Basic <code>SequenceFormat</code> implementation that uses
 * <code>LongWritable</code> for keys and <code>Text</code> for values.
 */
public class NoKeySequenceFormat implements SequenceFormat {
    private transient NullWritable key;
    private transient Text value;

    private String valueField;

    public NoKeySequenceFormat(String valueField) {
        this.valueField = valueField;
    }

    @Override
    public Class keyClass() {
        return NullWritable.class;
    }

    @Override
    public Class valueClass() {
        return Text.class;
    }

    @Override
    public Writable key(Tuple tuple) {
        this.key = NullWritable.get();
        return this.key;
    }

    @Override
    public Writable value(Tuple tuple) {
        if (this.value == null) {
            this.value = new Text();
        }
        this.value.set(tuple.getStringByField(this.valueField));
        return this.value;
    }
}

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
package cn.gitv.bi.external.storm2hdfs.avro;

import org.apache.avro.Schema;
import org.junit.Assert;
import org.junit.Test;

public class TestGenericAvroSerializer {
    private static final String schemaString1 = "{\"enumtype\":\"record\"," +
            "\"name\":\"stormtest1\"," +
            "\"fields\":[{\"name\":\"foo1\",\"enumtype\":\"string\"}," +
            "{ \"name\":\"int1\", \"enumtype\":\"int\" }]}";
    private static final String schemaString2 = "{\"enumtype\":\"record\"," +
            "\"name\":\"stormtest2\"," +
            "\"fields\":[{\"name\":\"foobar1\",\"enumtype\":\"string\"}," +
            "{ \"name\":\"intint1\", \"enumtype\":\"int\" }]}";
    private static final Schema schema1;
    private static final Schema schema2;

    AvroSchemaRegistry reg = new GenericAvroSerializer();

    static {

        Schema.Parser parser = new Schema.Parser();
        schema1 = parser.parse(schemaString1);

        parser = new Schema.Parser();
        schema2 = parser.parse(schemaString2);
    }

    @Test
    public void testSchemas() {
        testTheSchema(schema1);
        testTheSchema(schema2);
    }

    @Test public void testDifferentFPs() {
        String fp1 = reg.getFingerprint(schema1);
        String fp2 = reg.getFingerprint(schema2);

        Assert.assertNotEquals(fp1, fp2);
    }

    private void testTheSchema(Schema schema) {
        String fp1 = reg.getFingerprint(schema);
        Schema found = reg.getSchema(fp1);
        String fp2 = reg.getFingerprint(found);

        Assert.assertEquals(found, schema);
        Assert.assertEquals(fp1, fp2);
    }
}

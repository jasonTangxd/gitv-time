package cn.gitv.bi.viscosity.casstohdfs.utils;

import cn.gitv.bi.viscosity.casstohdfs.mapper.AvroBuilder;
import cn.gitv.bi.viscosity.casstohdfs.mapper.Mapper;
import cn.gitv.bi.viscosity.casstohdfs.mapper.ParquetBuilder;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.curator.framework.CuratorFramework;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.simple.SimpleGroupFactory;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.example.ExampleParquetWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static cn.gitv.bi.viscosity.casstohdfs.constant.ParquetSchema.*;

/**
 * Created by Kang on 2016/12/7.
 */
public class FileFormatUtils {
    private static Session session = null;
    private static CuratorFramework zk = null;
    private static Logger logger = LoggerFactory.getLogger(FileFormatUtils.class);

    public static void writeParquet(String table, String hdfsPath, String excuteCql, String excuteDate, String zkPath) {
        long startTime = System.currentTimeMillis();
        Configuration conf = new Configuration();
        FileSystem fs = null;
        try {
            fs = FileSystem.get(conf);
            Path hdfs_path = new Path(hdfsPath);
            session = CassandraConnection.getSession();
            ResultSet rs = session.execute(excuteCql);
            switch (table) {
                case "vod_viscosity_user":
                    SimpleGroupFactory simpleGroupFactory1 = new SimpleGroupFactory(VodUser_SCHEMA);
                    ParquetWriter<Group> writer1 = ExampleParquetWriter.builder(hdfs_path).withConf(conf).withType(VodUser_SCHEMA).build();
                    for (Row row : rs) {
                        writer1.write(ParquetBuilder.VOD_User_Row2Parquet(row, excuteDate, simpleGroupFactory1));
                    }
                    writer1.close();
                    break;
                case "vod_viscosity_program":
                    SimpleGroupFactory simpleGroupFactory2 = new SimpleGroupFactory(VodProgram_SCHEMA);
                    ParquetWriter<Group> writer2 = ExampleParquetWriter.builder(hdfs_path).withConf(conf).withType(VodProgram_SCHEMA).build();
                    for (Row row : rs) {
                        writer2.write(ParquetBuilder.VOD_Program_Row2Parquet(row, excuteDate, simpleGroupFactory2));
                    }
                    writer2.close();
                    break;
                case "liv_viscosity_user":
                    SimpleGroupFactory simpleGroupFactory3 = new SimpleGroupFactory(LivUser_SCHEMA);
                    ParquetWriter<Group> writer3 = ExampleParquetWriter.builder(hdfs_path).withConf(conf).withType(LivUser_SCHEMA).build();
                    for (Row row : rs) {
                        writer3.write(ParquetBuilder.LIV_User_Row2Parquet(row, excuteDate, simpleGroupFactory3));
                    }
                    writer3.close();
                    break;
                case "liv_viscosity_program":
                    SimpleGroupFactory simpleGroupFactory4 = new SimpleGroupFactory(LivProgram_SCHEMA);
                    ParquetWriter<Group> writer4 = ExampleParquetWriter.builder(hdfs_path).withConf(conf).withType(LivProgram_SCHEMA).build();
                    for (Row row : rs) {
                        writer4.write(ParquetBuilder.LIV_Program_Row2Parquet(row, excuteDate, simpleGroupFactory4));
                    }
                    writer4.close();
                    break;
            }
            logger.info("writeParquet: cass_table [{}],use time : {}", table, System.currentTimeMillis() - startTime);
            CassandraConnection.closeAll();
            fs.close();
            zk = CuratorTools.getSimpleCurator();
            zk.create().creatingParentsIfNeeded().forPath(zkPath, "successed!".getBytes());
            System.exit(0);
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            if (zk != null)
                zk.close();
            System.exit(0);
        }
    }

    public static void writeAvro(String table, String hdfsPath, String excuteCql, String excuteDate, String zkPath) {
        long startTime = System.currentTimeMillis();
        FSDataOutputStream out = null;
        Configuration conf = new Configuration();
        FileSystem fs = null;
        try {
            fs = FileSystem.get(conf);
            out = fs.create(new Path(hdfsPath));
            session = CassandraConnection.getSession();
            ResultSet rs = session.execute(excuteCql);
            switch (table) {
                case "vod_viscosity_user":
                    Schema.Parser parser1 = new Schema.Parser();
                    Schema schema1 = parser1.parse(FileFormatUtils.class.getResourceAsStream("/avro-meta/vod_user.avsc"));
                    DatumWriter<GenericRecord> writer1 = new GenericDatumWriter<>(schema1);
                    DataFileWriter<GenericRecord> dataFileWriter1 = new DataFileWriter<GenericRecord>(writer1);
                    dataFileWriter1.create(schema1, out);
                    for (Row row : rs) {
                        dataFileWriter1.append(AvroBuilder.VOD_User_Row2GenericRecord(row, excuteDate, schema1));
                    }
                    dataFileWriter1.close();
                    break;
                case "vod_viscosity_program":
                    Schema.Parser parser2 = new Schema.Parser();
                    Schema schema2 = parser2.parse(FileFormatUtils.class.getResourceAsStream("/avro-meta/vod_program.avsc"));
                    DatumWriter<GenericRecord> writer2 = new GenericDatumWriter<>(schema2);
                    DataFileWriter<GenericRecord> dataFileWriter2 = new DataFileWriter<GenericRecord>(writer2);
                    dataFileWriter2.create(schema2, out);
                    for (Row row : rs) {
                        dataFileWriter2.append(AvroBuilder.VOD_Program_Row2GenericRecord(row, excuteDate, schema2));
                    }
                    dataFileWriter2.close();
                    break;
                case "liv_viscosity_user":
                    Schema.Parser parser3 = new Schema.Parser();
                    Schema schema3 = parser3.parse(FileFormatUtils.class.getResourceAsStream("/avro-meta/liv_user.avsc"));
                    DatumWriter<GenericRecord> writer3 = new GenericDatumWriter<>(schema3);
                    DataFileWriter<GenericRecord> dataFileWriter3 = new DataFileWriter<GenericRecord>(writer3);
                    dataFileWriter3.create(schema3, out);
                    for (Row row : rs) {
                        dataFileWriter3.append(AvroBuilder.LIV_User_Row2GenericRecord(row, excuteDate, schema3));
                    }
                    dataFileWriter3.close();
                    break;
                case "liv_viscosity_program":
                    Schema.Parser parser4 = new Schema.Parser();
                    Schema schema4 = parser4.parse(FileFormatUtils.class.getResourceAsStream("/avro-meta/liv_program.avsc"));
                    DatumWriter<GenericRecord> writer4 = new GenericDatumWriter<>(schema4);
                    DataFileWriter<GenericRecord> dataFileWriter4 = new DataFileWriter<GenericRecord>(writer4);
                    dataFileWriter4.create(schema4, out);
                    for (Row row : rs) {
                        dataFileWriter4.append(AvroBuilder.LIV_Program_Row2GenericRecord(row, excuteDate, schema4));
                    }
                    dataFileWriter4.close();
                    break;
            }
            logger.info("writeAvro: cass_table [{}],use time : {}", table, System.currentTimeMillis() - startTime);
            CassandraConnection.closeAll();
            out.close();
            fs.close();
            zk = CuratorTools.getSimpleCurator();
            zk.create().creatingParentsIfNeeded().forPath(zkPath, "successed!".getBytes());
            System.exit(0);
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            if (zk != null)
                zk.close();
        }
        System.exit(0);
    }

    public static void writeText(String table, String hdfsPath, String excuteCql, String excuteDate, String zkPath) throws Exception {
        long startTime = System.currentTimeMillis();
        FSDataOutputStream out = null;
        try {
            session = CassandraConnection.getSession();
            Configuration conf = new Configuration();
            FileSystem fs = FileSystem.get(conf);
            out = fs.create(new Path(hdfsPath));
            ResultSet rs = session.execute(excuteCql);
            switch (table) {
                case "vod_viscosity_user":
                    for (Row row : rs) {
                        out.write(Mapper.VOD_User_RowToString(row, excuteDate).getBytes());
                    }
                    break;
                case "vod_viscosity_program":
                    for (Row row : rs) {
                        out.write(Mapper.VOD_Program_RowToString(row, excuteDate).getBytes());
                    }
                    break;
                case "liv_viscosity_user":
                    for (Row row : rs) {
                        out.write(Mapper.LIV_User_RowToString(row, excuteDate).getBytes());
                    }
                    break;
                case "liv_viscosity_program":
                    for (Row row : rs) {
                        out.write(Mapper.LIV_Program_RowToString(row, excuteDate).getBytes());
                    }
                    break;
            }
            logger.info("writeText: cass_table [{}],use time : {}", table, System.currentTimeMillis() - startTime);
            CassandraConnection.closeAll();
            out.close();
            fs.close();
            zk = CuratorTools.getSimpleCurator();
            zk.create().creatingParentsIfNeeded().forPath(zkPath, "successed!".getBytes());
            System.exit(0);
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            if (zk != null)
                zk.close();
        }
        System.exit(0);
    }

    public static void writeSeq(String table, String hdfsPath, String excuteCql, String excuteDate, String zkPath) throws Exception {
        long startTime = System.currentTimeMillis();
        SequenceFile.Writer writer;
        LongWritable key = new LongWritable();
        Text value = new Text();
        try {
            session = CassandraConnection.getSession();
            Configuration conf = new Configuration();
            CompressionCodecFactory codecFactory = new CompressionCodecFactory(conf);
            writer = SequenceFile.createWriter(conf, SequenceFile.Writer.file(new Path(hdfsPath)),
                    SequenceFile.Writer.keyClass(LongWritable.class), SequenceFile.Writer.valueClass(Text.class),
                    SequenceFile.Writer.compression(SequenceFile.CompressionType.RECORD, codecFactory.getCodecByName("default")));
            ResultSet rs = session.execute(excuteCql);
            switch (table) {
                case "vod_viscosity_user":
                    for (Row row : rs) {
                        key.set(System.currentTimeMillis());
                        value.set(Mapper.VOD_User_RowToString(row, excuteDate).getBytes());
                        writer.append(key, value);
                    }
                    break;
                case "vod_viscosity_program":
                    for (Row row : rs) {
                        key.set(System.currentTimeMillis());
                        value.set(Mapper.VOD_Program_RowToString(row, excuteDate).getBytes());
                        writer.append(key, value);
                    }
                    break;
                case "liv_viscosity_user":
                    for (Row row : rs) {
                        key.set(System.currentTimeMillis());
                        value.set(Mapper.LIV_User_RowToString(row, excuteDate).getBytes());
                        writer.append(key, value);
                    }
                    break;
                case "liv_viscosity_program":
                    for (Row row : rs) {
                        key.set(System.currentTimeMillis());
                        value.set(Mapper.LIV_Program_RowToString(row, excuteDate).getBytes());
                        writer.append(key, value);
                    }
                    break;
            }
            logger.info("writeSeq: cass_table [{}],use time : {}", table, System.currentTimeMillis() - startTime);
            CassandraConnection.closeAll();
            writer.close();
            zk = CuratorTools.getSimpleCurator();
            zk.create().creatingParentsIfNeeded().forPath(zkPath, "successed!".getBytes());
            System.exit(0);
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            if (zk != null)
                zk.close();
            System.exit(0);
        }
    }
}

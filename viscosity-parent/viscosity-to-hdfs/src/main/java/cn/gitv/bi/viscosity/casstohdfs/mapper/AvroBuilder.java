package cn.gitv.bi.viscosity.casstohdfs.mapper;

import com.datastax.driver.core.Row;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;

/**
 * Created by Kang on 2016/12/7.
 */
public class AvroBuilder {
    public static GenericRecord VOD_User_Row2GenericRecord(Row row, String date, Schema schema) throws Exception {
        GenericRecord genericRecord = new GenericData.Record(schema);
        genericRecord.put("partner", row.getString(0));
        genericRecord.put("logdate", date);
        genericRecord.put("mac", row.getString(2));
        genericRecord.put("srcname", row.getString(3));
        genericRecord.put("chnname", row.getString(4));
        genericRecord.put("chnId", row.getString(5));
        genericRecord.put("albumname", row.getString(6));
        genericRecord.put("albumId", row.getString(7));
        genericRecord.put("playorder", row.getString(8));
        genericRecord.put("videoId", row.getString(9));
        genericRecord.put("click_num", row.getLong(10));
        genericRecord.put("playlength", row.getLong(11));
        genericRecord.put("timelength", row.getLong(12));
        return genericRecord;
    }

    public static GenericRecord VOD_Program_Row2GenericRecord(Row row, String date, Schema schema) throws Exception {
        GenericRecord genericRecord = new GenericData.Record(schema);
        genericRecord.put("partner", row.getString(0));
        genericRecord.put("logdate", date);
        genericRecord.put("srcname", row.getString(2));
        genericRecord.put("chnname", row.getString(3));
        genericRecord.put("chnId", row.getString(4));
        genericRecord.put("albumname", row.getString(5));
        genericRecord.put("albumId", row.getString(6));
        genericRecord.put("playorder", row.getString(7));
        genericRecord.put("videoId", row.getString(8));
        genericRecord.put("province", row.getString(9));
        genericRecord.put("city", row.getString(10));
        genericRecord.put("click_num", row.getLong(11));
        genericRecord.put("playlength", row.getLong(12));
        genericRecord.put("timelength", row.getLong(13));
        return genericRecord;
    }

    public static GenericRecord LIV_User_Row2GenericRecord(Row row, String date, Schema schema) throws Exception {
        GenericRecord genericRecord = new GenericData.Record(schema);
        genericRecord.put("partner", row.getString(0));
        genericRecord.put("logdate", date);
        genericRecord.put("mac", row.getString(2));
        genericRecord.put("srcname", row.getString(3));
        genericRecord.put("chncode", row.getString(4));
        genericRecord.put("albumname", row.getString(5));
        genericRecord.put("playorder", row.getString(6));
        genericRecord.put("click_num", row.getLong(7));
        genericRecord.put("playlength", row.getLong(8));
        genericRecord.put("timelength", row.getLong(9));
        return genericRecord;
    }

    public static GenericRecord LIV_Program_Row2GenericRecord(Row row, String date, Schema schema) throws Exception {
        GenericRecord genericRecord = new GenericData.Record(schema);
        genericRecord.put("partner", row.getString(0));
        genericRecord.put("logdate", date);
        genericRecord.put("srcname", row.getString(2));
        genericRecord.put("chncode", row.getString(3));
        genericRecord.put("albumname", row.getString(4));
        genericRecord.put("playorder", row.getString(5));
        genericRecord.put("province", row.getString(6));
        genericRecord.put("city", row.getString(7));
        genericRecord.put("click_num", row.getLong(8));
        genericRecord.put("playlength", row.getLong(9));
        genericRecord.put("timelength", row.getLong(10));
        return genericRecord;
    }

}


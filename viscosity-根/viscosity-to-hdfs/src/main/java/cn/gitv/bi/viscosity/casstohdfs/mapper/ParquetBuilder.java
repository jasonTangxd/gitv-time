package cn.gitv.bi.viscosity.casstohdfs.mapper;

import com.datastax.driver.core.Row;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.simple.SimpleGroupFactory;

/**
 * Created by Kang on 2016/12/7.
 */
public class ParquetBuilder {
    public static Group VOD_User_Row2Parquet(Row row, String date, SimpleGroupFactory simpleGroupFactory) throws Exception {
        Group group = simpleGroupFactory.newGroup();
        group.add("partner", row.getString(0));
        group.add("logdate", date);
        group.add("mac", row.getString(2));
        group.add("srcname", row.getString(3));
        group.add("chnname", row.getString(4));
        group.add("chnId", row.getString(5));
        group.add("albumname", row.getString(6));
        group.add("albumId", row.getString(7));
        group.add("playorder", row.getString(8));
        group.add("videoId", row.getString(9));
        group.add("click_num", row.getLong(10));
        group.add("playlength", row.getLong(11));
        group.add("timelength", row.getLong(12));
        return group;
    }

    public static Group VOD_Program_Row2Parquet(Row row, String date, SimpleGroupFactory simpleGroupFactory) throws Exception {
        Group group = simpleGroupFactory.newGroup();
        group.add("partner", row.getString(0));
        group.add("logdate", date);
        group.add("srcname", row.getString(2));
        group.add("chnname", row.getString(3));
        group.add("chnId", row.getString(4));
        group.add("albumname", row.getString(5));
        group.add("albumId", row.getString(6));
        group.add("playorder", row.getString(7));
        group.add("videoId", row.getString(8));
        group.add("province", row.getString(9));
        group.add("city", row.getString(10));
        group.add("click_num", row.getLong(11));
        group.add("playlength", row.getLong(12));
        group.add("timelength", row.getLong(13));
        return group;
    }

    public static Group LIV_User_Row2Parquet(Row row, String date, SimpleGroupFactory simpleGroupFactory) throws Exception {
        Group group = simpleGroupFactory.newGroup();
        group.add("partner", row.getString(0));
        group.add("logdate", date);
        group.add("mac", row.getString(2));
        group.add("srcname", row.getString(3));
        group.add("chncode", row.getString(4));
        group.add("albumname", row.getString(5));
        group.add("playorder", row.getString(6));
        group.add("click_num", row.getLong(7));
        group.add("playlength", row.getLong(8));
        group.add("timelength", row.getLong(9));
        return group;
    }

    public static Group LIV_Program_Row2Parquet(Row row, String date, SimpleGroupFactory simpleGroupFactory) throws Exception {
        Group group = simpleGroupFactory.newGroup();
        group.add("partner", row.getString(0));
        group.add("logdate", date);
        group.add("srcname", row.getString(2));
        group.add("chncode", row.getString(3));
        group.add("albumname", row.getString(4));
        group.add("playorder", row.getString(5));
        group.add("province", row.getString(6));
        group.add("city", row.getString(7));
        group.add("click_num", row.getLong(8));
        group.add("playlength", row.getLong(9));
        group.add("timelength", row.getLong(10));
        return group;
    }

}


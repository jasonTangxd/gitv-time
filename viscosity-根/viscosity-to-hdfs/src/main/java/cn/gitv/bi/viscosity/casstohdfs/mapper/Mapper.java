package cn.gitv.bi.viscosity.casstohdfs.mapper;

import com.datastax.driver.core.Row;

import static cn.gitv.bi.viscosity.casstohdfs.constant.Constant.SEPARATOR;

public class Mapper {
    public static String VOD_User_RowToString(Row row, String date) {
        StringBuilder sb = new StringBuilder();
        sb.append(row.getString(0)).append(SEPARATOR).append(date).append(SEPARATOR)
                .append(row.getString(2)).append(SEPARATOR).append(row.getString(3)).append(SEPARATOR)
                .append(row.getString(4)).append(SEPARATOR).append(row.getString(5)).append(SEPARATOR)
                .append(row.getString(6)).append(SEPARATOR).append(row.getString(7)).append(SEPARATOR)
                .append(row.getString(8)).append(SEPARATOR).append(row.getString(9)).append(SEPARATOR)
                .append(row.getLong(10)).append(SEPARATOR).append(row.getLong(11)).append(SEPARATOR).append(row.getLong(12)).append("\n");
        return sb.toString();
    }

    public static String VOD_Program_RowToString(Row row, String date) {
        StringBuilder sb = new StringBuilder();
        sb.append(row.getString(0)).append(SEPARATOR).append(date).append(SEPARATOR).append(row.getString(2))
                .append(SEPARATOR).append(row.getString(3)).append(SEPARATOR).append(row.getString(4))
                .append(SEPARATOR).append(row.getString(5)).append(SEPARATOR).append(row.getString(6))
                .append(SEPARATOR).append(row.getString(7)).append(SEPARATOR).append(row.getString(8))
                .append(SEPARATOR).append(row.getString(9)).append(SEPARATOR).append(row.getString(10)).append(SEPARATOR)
                .append(row.getLong(11)).append(SEPARATOR).append(row.getLong(12)).append(SEPARATOR).append(row.getLong(13)).append("\n");
        return sb.toString();
    }

    public static String LIV_User_RowToString(Row row, String date) {
        StringBuilder sb = new StringBuilder();
        sb.append(row.getString(0)).append(SEPARATOR).append(date).append(SEPARATOR)
                .append(row.getString(2)).append(SEPARATOR).append(row.getString(3)).append(SEPARATOR)
                .append(row.getString(4)).append(SEPARATOR).append(row.getString(5)).append(SEPARATOR)
                .append(row.getString(6)).append(SEPARATOR).append(row.getLong(7)).append(SEPARATOR).append(row.getLong(8)).append(SEPARATOR).append(row.getLong(9)).append("\n");
        return sb.toString();
    }

    public static String LIV_Program_RowToString(Row row, String date) {
        StringBuilder sb = new StringBuilder();
        sb.append(row.getString(0)).append(SEPARATOR).append(date).append(SEPARATOR).append(row.getString(2))
                .append(SEPARATOR).append(row.getString(3)).append(SEPARATOR).append(row.getString(4))
                .append(SEPARATOR).append(row.getString(5)).append(SEPARATOR).append(row.getString(6))
                .append(SEPARATOR).append(row.getString(7)).append(SEPARATOR).
                append(row.getLong(8)).append(SEPARATOR).append(row.getLong(9)).append(SEPARATOR).append(row.getLong(10)).append("\n");
        return sb.toString();
    }
}

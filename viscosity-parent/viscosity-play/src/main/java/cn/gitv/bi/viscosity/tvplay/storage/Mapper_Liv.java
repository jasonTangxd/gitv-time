package cn.gitv.bi.viscosity.tvplay.storage;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;

import java.util.Date;

public class Mapper_Liv {

    public static BoundStatement read_program(PreparedStatement ps, String partner, String srcName, String chnCode,
                                              String albumName, String playOrder, String province, String city, Date logDate) {
        return ps.bind(partner, srcName, chnCode, albumName, playOrder, province, city, logDate);
    }

    public static BoundStatement insert_new_program(PreparedStatement ps, String partner, String srcName,
                                                    String chnCode, String albumName, String playOrder, String province, String city, long playLength,
                                                    long timeLength, Date logDate) {
        return ps.bind(playLength, timeLength, partner, srcName, chnCode, albumName, playOrder, province, city,
                logDate);
    }

    public static BoundStatement update_new_program(PreparedStatement ps, String partner, String srcName,
                                                    String chnCode, String albumName, String playOrder, String province, String city, long playLength,
                                                    Date logDate) {
        return ps.bind(playLength, partner, srcName, chnCode, albumName, playOrder, province, city, logDate);
    }

    // ----
    public static BoundStatement read_user(PreparedStatement ps, String partner, String mac, String srcName,
                                           String chnCode, String albumName, String playOrder, Date logDate) {
        return ps.bind(partner, mac, srcName, chnCode, albumName, playOrder, logDate);
    }

    public static BoundStatement insert_new_user(PreparedStatement ps, String partner, String mac, String srcName,
                                                 String chnCode, String albumName, String playOrder, long playLength, long timeLength, Date logDate) {
        return ps.bind(playLength, timeLength, partner, mac, srcName, chnCode, albumName, playOrder, logDate);
    }

    public static BoundStatement update_new_user(PreparedStatement ps, String partner, String mac, String srcName,
                                                 String chnCode, String albumName, String playOrder, long playLength, Date logDate) {
        return ps.bind(playLength, partner, mac, srcName, chnCode, albumName, playOrder, logDate);
    }

}
package cn.gitv.bi.viscosity.tvplay.storage;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;

import java.util.Date;

public class Mapper {
    public static BoundStatement pre_nosrcid_mean(PreparedStatement ps, String srcid) {
        return ps.bind(srcid);
    }

    public static BoundStatement read_program(PreparedStatement ps, String partner, String srcName, String chnName, String chnId,
                                              String albumName, String albumId, String playOrder, String videoId, String province, String city, Date logDate) {
        return ps.bind(partner, srcName, chnName, chnId, albumName, albumId, playOrder, videoId, province, city, logDate);
    }

    public static BoundStatement insert_new_program(PreparedStatement ps, String partner, String srcName,
                                                    String chnName, String chnId, String albumName, String albumId, String playOrder, String videoId, String province, String city, long playLength,
                                                    long timeLength, Date logDate) {
        return ps.bind(playLength, timeLength, partner, srcName, chnName, chnId, albumName, albumId, playOrder, videoId, province, city,
                logDate);
    }

    public static BoundStatement update_new_program(PreparedStatement ps, String partner, String srcName,
                                                    String chnName, String chnId, String albumName, String albumId, String playOrder, String videoId, String province, String city, long playLength,
                                                    Date logDate) {
        return ps.bind(playLength, partner, srcName, chnName, chnId, albumName, albumId, playOrder, videoId, province, city, logDate);
    }

    // ----
    public static BoundStatement read_user(PreparedStatement ps, String partner, String mac, String srcName,
                                           String chnName, String chnId, String albumName, String albumId, String playOrder, String videoId, Date logDate) {
        return ps.bind(partner, mac, srcName, chnName, chnId, albumName, albumId, playOrder, videoId, logDate);
    }

    public static BoundStatement insert_new_user(PreparedStatement ps, String partner, String mac, String srcName,
                                                 String chnName, String chnId, String albumName, String albumId, String playOrder, String videoId, long playLength, long timeLength, Date logDate) {
        return ps.bind(playLength, timeLength, partner, mac, srcName, chnName, chnId, albumName, albumId, playOrder, videoId, logDate);
    }

    public static BoundStatement update_new_user(PreparedStatement ps, String partner, String mac, String srcName,
                                                 String chnName, String chnId, String albumName, String albumId, String playOrder, String videoId, long playLength, Date logDate) {
        return ps.bind(playLength, partner, mac, srcName, chnName, chnId, albumName, albumId, playOrder, videoId, logDate);
    }

}
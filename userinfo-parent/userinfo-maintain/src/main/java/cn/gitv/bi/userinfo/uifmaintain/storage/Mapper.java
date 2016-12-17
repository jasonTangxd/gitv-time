package cn.gitv.bi.userinfo.uifmaintain.storage;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;

import java.util.Date;

public class Mapper {
    public static BoundStatement noPToProvince(PreparedStatement ps, String partner) {
        return ps.bind(partner);
    }

    //
    public static BoundStatement main_isnull(PreparedStatement ps, String partner, String mac) {
        return ps.bind(partner, mac);
    }

    //
    public static BoundStatement fopen_isnull(PreparedStatement ps, String partner, String mac) {
        return ps.bind(partner, mac);
    }

    //
    public static BoundStatement fopen_update(PreparedStatement ps, Date first_open, Date record_date, String partner,
                                              String mac) {
        return ps.bind(first_open, record_date, partner, mac);
    }

    //
    public static BoundStatement appv_update(PreparedStatement ps, String partner, String mac,
                                             String app_version) {
        return ps.bind(app_version, partner, mac);
    }

    //
    public static BoundStatement nojudge_update(PreparedStatement ps, String partner, String mac, Date last_open,
                                                String province) {
        return ps.bind(province, last_open, partner, mac);
    }
}

package cn.gitv.bi.userinfo.appupdate.storage;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;

public class Mapper {
	public static BoundStatement read_appVersion(PreparedStatement ps, String partner, String mac_addr) {
		return ps.bind(partner, mac_addr);
	}

	public static String update_uif_map_cql(String cql, String app_version, String log_time, String partner,String mac_addr) {
		return String.format(cql, app_version, app_version, log_time, partner, mac_addr);
	}
}

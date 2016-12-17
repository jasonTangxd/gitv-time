package cn.gitv.bi.userinfo.rmconsumer.storage;

import cn.gitv.bi.userinfo.rmconsumer.bean.UserInfo;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;

public class Mapper {
    public static BoundStatement update_uif(PreparedStatement ps, UserInfo item) {
        return ps.bind(item.getMain_account_id(), item.getChild_account_id(), item.getUser_id(), item.getAccount_time(), item.getActivate_time(), item.getStatus(), item.getUser_type(),
                item.getProvince(), item.getCity_name(), item.getArea_name(), item.getPartner(), item.getMac_addr());
    }
}

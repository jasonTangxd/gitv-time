package cn.gitv.bi.userinfo.rmconsumer.thread;

import cn.gitv.bi.userinfo.rmconsumer.bean.UserInfo;
import cn.gitv.bi.userinfo.rmconsumer.syndata.Super_SynData;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.List;
import java.util.Set;

/**
 * Created by Kang on 2016/12/7.
 */
public class TransRuner implements Runnable {
    private Logger logger = LoggerFactory.getLogger(TransRuner.class);
    private Set<String> mac_list;
    private Super_SynData synInstance;
    private Connection connection;
    private Session session;
    private PreparedStatement ps;

    public TransRuner(Set<String> mac_list, Super_SynData synInstance, Connection connection, Session session, PreparedStatement ps) {
        this.mac_list = mac_list;
        this.synInstance = synInstance;
        this.connection = connection;
        this.session = session;
        this.ps = ps;
    }

    @Override
    public void run() {
        try {
            List<UserInfo> userList = synInstance.getFromMysql(mac_list, connection);
            if (userList != null)
                synInstance.upToCassandra(userList, session, ps);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

}

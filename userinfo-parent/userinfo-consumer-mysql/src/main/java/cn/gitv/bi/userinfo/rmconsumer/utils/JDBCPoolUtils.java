package cn.gitv.bi.userinfo.rmconsumer.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class JDBCPoolUtils {
    private static Logger logger = LoggerFactory.getLogger(JDBCPoolUtils.class);
    private ComboPooledDataSource cpds = null;

    public JDBCPoolUtils(String partner) {
        cpds = new ComboPooledDataSource(partner);
    }

    /**
     * @return jdbc connection
     */
    public synchronized Connection getConnection() throws SQLException, ClassNotFoundException, InterruptedException {
        return cpds.getConnection();
    }

    public static synchronized Connection getConnection(String routingKey) {
        try {
            JDBCPoolUtils jdbcPoolUtils = new JDBCPoolUtils(routingKey);
            return jdbcPoolUtils.getConnection();
        } catch (Exception e) {
            logger.error("", e);
        }
        return null;
    }

}
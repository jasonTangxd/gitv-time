package cn.gitv.bi.userinfo.rmconsumer.test;

import cn.gitv.bi.userinfo.rmconsumer.utils.CassandraConnection;
import cn.gitv.bi.userinfo.rmconsumer.utils.JDBCPoolUtils;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Iterator;

/**
 * Created by komlei on 16-12-20.
 */
public class HowJSCPW {
    private static Session session = CassandraConnection.getSession();
    private static Connection connection = JDBCPoolUtils.getConnection("JS_CMCC_CP");
    private static final String sql = "select USER_ID from active_success where MAC=?;";
    private static PreparedStatement ps = null;
    private final static String cql = "select main_account_id,mac_addr from userinfo.user_info where partner='JS_CMCC_CP';";

    public static void main(String args[]) throws Exception {
        int total = 0;
        int mysqlNum = 0;
        ps = connection.prepareStatement(sql);
        ResultSet execute = session.execute(cql);
        Iterator<Row> it = execute.iterator();
        while (it.hasNext()) {
            Row item = it.next();
            String mac = item.getString("mac_addr").toUpperCase();
            String main_account = item.getString("main_account_id");
            if (main_account == null) {
                total++;
                //所有main_account为null的mac
                ps.setString(1, mac);
                java.sql.ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    String uid = resultSet.getString(1);
                    mysqlNum++;
                }
            }
        }
        System.out.println("total is:" + total);
        System.out.println("mysqlNum is:" + mysqlNum);
            /*total is:67275        mysqlNum is:2537*/
    }
}

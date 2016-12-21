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
public class HowAHW {
    private static Session session = CassandraConnection.getSession();
    private static Connection connection = JDBCPoolUtils.getConnection("AH_CMCC");
    private static final String sql = "select MAIN_ACCOUNT_ID,CHILD_ACCOUNT_ID,USER_TYPE,CREATE_TIME from cont_user_mac where MAC_ADDR=?;";
    private final static String cql = "select main_account_id,mac_addr from userinfo.user_info where partner='AH_CMCC';";
    private static PreparedStatement ps = null;

    public static void main(String args[]) throws Exception {
//        int all_total=0;
        int total = 0;
        int mysqlNum = 0;
        ps = connection.prepareStatement(sql);
        ResultSet execute = session.execute(cql);
        Iterator<Row> it = execute.iterator();
        while (it.hasNext()) {
            Row item = it.next();
            String mac = item.getString("mac_addr");
            String main_account = item.getString("main_account_id");
            if (main_account == null) {
                total++;
                //所有main_account为null的mac
                ps.setString(1, mac);
                java.sql.ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    System.out.println(resultSet.getString("MAIN_ACCOUNT_ID"));
                    mysqlNum++;
//                }
                }
            }
            System.out.println("total is:" + total);
            System.out.println("mysql is:" + mysqlNum);

        }
    }
}

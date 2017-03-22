package cn.gitv.bi.userinfo.rmconsumer.test;

import cn.gitv.bi.userinfo.rmconsumer.bean.UserInfo;
import cn.gitv.bi.userinfo.rmconsumer.storage.Mapper;
import cn.gitv.bi.userinfo.rmconsumer.utils.CassandraConnection;
import cn.gitv.bi.userinfo.rmconsumer.utils.JDBCPoolUtils;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by komlei on 16-12-20.
 */
public class HowJSWBC {
    private static Session session = CassandraConnection.getSession();
    private static Connection connection = JDBCPoolUtils.getConnection("JS_CMCC");
    private static final String sql = "select phone_number,user_city,user_type,status,first_login,create_date from hdc_ordering_relation_info where mac=?;";
    // 该表的create_date字段作为开户时间
    private static final String sql2 = "select name from city where id=?;";
    private static final String sql3 = "select DEV_CTIME from device where DEV_MAC=?;";// 获取用户的激活时间
    private static PreparedStatement ps = null;
    private static PreparedStatement ps2 = null;
    private static PreparedStatement ps3 = null;
    private static final String UPDATE_UIF = "update user_info set main_account_id=?,child_account_id=?,user_id=?,account_time=?,activate_time=?,status=?,user_type=?,province=?,city_name=?,area_name=? where partner=? and mac_addr=?;";
    private final static String cql = "select main_account_id,mac_addr from userinfo.user_info where partner='JS_CMCC';";
    private static com.datastax.driver.core.PreparedStatement pc = session.prepare(UPDATE_UIF);

    public static void main(String args[]) throws Exception {
        int total = 0;
        int mysqlNum = 0;
        ps = connection.prepareStatement(sql);
        ps2 = connection.prepareStatement(sql2);
        ps3 = connection.prepareStatement(sql3);
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
                java.sql.ResultSet executeQuery1 = ps.executeQuery();
                if (executeQuery1.next()) {
                    UserInfo uif = new UserInfo();
                    uif = new UserInfo();
                    uif.setPartner("JS_CMCC");
                    uif.setProvince("江苏省");
                    uif.setMac_addr(mac);//
                    uif.setMain_account_id(executeQuery1.getString(1));//
                    uif.setChild_account_id(executeQuery1.getString(1));//
                    uif.setUser_id(executeQuery1.getString(1));//
                    String user_city = executeQuery1.getString(2);// user_city
                    ps2.setString(1, user_city);
                    java.sql.ResultSet executeQuery2 = ps2.executeQuery();
                    if (executeQuery2.next()) {
                        String city = executeQuery2.getString(1);
                        if (city.endsWith("市")) {
                            city = city.substring(0, city.length() - 1);
                        }
                        uif.setCity_name(city);
                    }
                    executeQuery2.close();
                    uif.setUser_type(executeQuery1.getInt(3));
                    int status = executeQuery1.getInt(4);// 0为暂停，1为启用
                    int first_login = executeQuery1.getInt(5);// 1表示首次登陆<未激活>
                    // 0表示非首次登陆<已激活>
                    if (first_login == 1 && status == 1) {
                        uif.setStatus(1);// 未激活的启用是开户
                    } else if (first_login == 0 && status == 1) {
                        uif.setStatus(6);// 激活的启动是激活
                    } else if (first_login == 0 && status == 0) {
                        uif.setStatus(4);// 激活的暂停是暂停
                    } else {
                        uif.setStatus(0);// 若为0则代表null
                    }
                    // first_login == 1 && status == 0不存在
                    uif.setAccount_time(new Date(executeQuery1.getTimestamp(6).getTime()));
                    executeQuery1.close();
                    ps3.setString(1, uif.getMac_addr());
                    java.sql.ResultSet executeQuery3 = ps3.executeQuery();
                    if (executeQuery3.next()) {
                        uif.setActivate_time(new Date(executeQuery3.getTimestamp(1).getTime()));
                    }
                    executeQuery3.close();
                    BoundStatement bs = Mapper.update_uif(pc, uif);
                    session.execute(bs);
                }
            }
        }
        System.out.println("total is:" + total);
    }
}

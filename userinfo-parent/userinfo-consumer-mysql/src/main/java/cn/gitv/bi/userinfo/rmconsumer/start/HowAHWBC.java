package cn.gitv.bi.userinfo.rmconsumer.start;

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
public class HowAHWBC {
    private static Session session = CassandraConnection.getSession();
    private static Connection connection = JDBCPoolUtils.getConnection("AH_CMCC");
    private static final String sql1 = "select MAIN_ACCOUNT_ID,CHILD_ACCOUNT_ID,USER_TYPE,CREATE_TIME from cont_user_mac where MAC_ADDR=?;";
    private static final String sql2 = "select AREA_NO,STATUS,CREATE_TIME from cont_user_child where USER_ID=?;";
    private static final String sql3 = "select AREA_NAME,CITY_NAME from cont_user_area_info where AREA_CODE=?;";
    private static PreparedStatement ps1 = null;
    private static PreparedStatement ps2 = null;
    private static PreparedStatement ps3 = null;
    private static final String UPDATE_UIF = "update user_info set main_account_id=?,child_account_id=?,user_id=?,account_time=?,activate_time=?,status=?,user_type=?,province=?,city_name=?,area_name=? where partner=? and mac_addr=?;";
    private final static String cql = "select main_account_id,mac_addr from userinfo.user_info where partner='AH_CMCC';";
    private static com.datastax.driver.core.PreparedStatement pc = session.prepare(UPDATE_UIF);

    public static void main(String args[]) throws Exception {
        int total = 0;
        int mysqlNum = 0;
        ps1 = connection.prepareStatement(sql1);
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
                System.out.println(mac);
                //所有main_account为null的mac
                ps1.setString(1, mac);
                java.sql.ResultSet resultSet = ps1.executeQuery();
                if (resultSet.next()) {
                    UserInfo uif = new UserInfo();
                    uif.setPartner("AH_CMCC");
                    uif.setProvince("安徽省");
                    uif.setMac_addr(mac);//
                    uif.setMain_account_id(resultSet.getString(1));//
                    uif.setChild_account_id(resultSet.getString(2));//
                    uif.setUser_id(resultSet.getString(2));//
                    uif.setUser_type(resultSet.getInt(3));// 用户类型(1:普客 2:集客)
                    uif.setActivate_time(new Date(resultSet.getTimestamp(4).getTime()));// 首次激活时间
                    ps2.setString(1, uif.getChild_account_id());//
                    java.sql.ResultSet executeQuery2 = ps2.executeQuery();
                    if (executeQuery2.next()) {
                        String AREA_NO = executeQuery2.getString(1);
                        ps3.setString(1, AREA_NO);
                        java.sql.ResultSet executeQuery3 = ps3.executeQuery();
                        if (executeQuery3.next()) {
                            uif.setArea_name(executeQuery3.getString(1));//
                            String city = executeQuery3.getString(2);
                            if (city.endsWith("市")) {
                                city = city.substring(0, city.length() - 1);
                            }
                            uif.setCity_name(city);//
                        }
                        executeQuery3.close();
                        int status = executeQuery2.getInt(2);// 1=开户 2=销户 3=换机
                        // 4=暂停 5=复机
                        if (status == 1) {
                            uif.setStatus(6);
                        } else {
                            uif.setStatus(status);
                        }
                        uif.setAccount_time(new Date(executeQuery2.getTimestamp(3).getTime()));// 开户时间
                        // cont_user_child的CREATE_TIME用作开户时间
                    }
                    System.out.println(uif);
                    executeQuery2.close();
                    resultSet.close();
                    BoundStatement bs = Mapper.update_uif(pc, uif);
                    session.execute(bs);
                }
            }
        }
        System.out.println("total is:" + total);
        System.out.println("mysqlNum is:" + mysqlNum);
    }
}

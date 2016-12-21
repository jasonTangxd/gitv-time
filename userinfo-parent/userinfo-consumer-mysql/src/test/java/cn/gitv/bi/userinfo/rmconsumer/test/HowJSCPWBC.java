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
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by komlei on 16-12-20.
 */
public class HowJSCPWBC {
    private static Session session = CassandraConnection.getSession();
    private static Connection connection = JDBCPoolUtils.getConnection("JS_CMCC_CP");
    private static final String sql = "select USER_ID,CREATE_TIME from active_success where MAC=?;";
    private static final String sql2 = "select userCity,createTime,opType,idType from order_relation where phoneNumber=?;";
    private static final String sql3 = "select name from city where id=?;";
    private static PreparedStatement ps = null;
    private static PreparedStatement ps2 = null;
    private static PreparedStatement ps3 = null;
    private static final String UPDATE_UIF = "update user_info set main_account_id=?,child_account_id=?,user_id=?,account_time=?,activate_time=?,status=?,user_type=?,province=?,city_name=?,area_name=? where partner=? and mac_addr=?;";
    private final static String cql = "select main_account_id,mac_addr from userinfo.user_info where partner='JS_CMCC_CP';";
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
                java.sql.ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    UserInfo uif = new UserInfo();
                    uif.setPartner("JS_CMCC_CP");
                    uif.setProvince("江苏省");
                    uif.setMac_addr(mac);//
                    uif.setMain_account_id(resultSet.getString(1));//
                    uif.setChild_account_id(resultSet.getString(1));//
                    uif.setUser_id(resultSet.getString(1));//
                    uif.setActivate_time(new Date(resultSet.getTimestamp(2).getTime()));
                    ps2.setString(1, uif.getUser_id());
                    java.sql.ResultSet executeQuery2 = ps2.executeQuery();
                    if (executeQuery2.next()) {
                        String userCity = executeQuery2.getString(1);
                        uif.setAccount_time(new Date(executeQuery2.getTimestamp(2).getTime()));
                        ps3.setString(1, userCity);
                        java.sql.ResultSet executeQuery3 = ps3.executeQuery();
                        if (executeQuery3.next()) {
                            String city = executeQuery3.getString(1);
                            if (city.endsWith("市")) {
                                city = city.substring(0, city.length() - 1);
                            }
                            uif.setCity_name(city);
                        }
                        executeQuery3.close();
                        String opType = executeQuery2.getString(3);
                        // r0:
                        // 订购(开户)\r1：取消订购（销户）\r注:集团客户消户，将客户下所有用户做销户处理\r2：暂停\r3：恢复\r4：密码重置\r5:
                        // 变更\r6：激活\r7：拆机\r8：停机
                        if (opType.equals("0")) {
                            uif.setStatus(1);
                        } else if (opType.equals("1")) {
                            uif.setStatus(2);
                        } else if (opType.equals("2")) {
                            uif.setStatus(4);
                        } else if (opType.equals("3")) {
                            uif.setStatus(5);
                        } else if (opType.equals("6")) {
                            uif.setStatus(6);
                        } else if (opType.equals("8")) {
                            uif.setStatus(7);
                        } else {
                            uif.setStatus(0);
                        }
                        String idType = executeQuery2.getString(4);
                        // 标识类型\rCRM侧表示类型。\r01:手机号码-->1 \r91:集团客户编码-->2
                        // \r92:集团成员帐号-->3
                        if (idType.equals("01")) {
                            uif.setUser_type(1);
                        } else if (idType.equals("91")) {
                            uif.setUser_type(2);
                        } else if (idType.equals("92")) {
                            uif.setUser_type(3);
                        } else {
                            uif.setUser_type(0);
                        }
                    }
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

package cn.gitv.bi.userinfo.rmconsumer.syndata;

import cn.gitv.bi.userinfo.rmconsumer.bean.UserInfo;
import cn.gitv.bi.userinfo.rmconsumer.utils.JDBCPoolUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

public class JS_CMCC_SynData extends SuperSynData {
    private String partner = null;
    private static final String sql1 = "select phone_number,user_city,user_type,status,first_login,create_date from hdc_ordering_relation_info where mac=?;";
    // 该表的create_date字段作为开户时间
    private static final String sql2 = "select name from city where id=?;";
    private static final String sql3 = "select DEV_CTIME from device where DEV_MAC=?;";// 获取用户的激活时间
    private PreparedStatement ps1 = null;
    private PreparedStatement ps2 = null;
    private PreparedStatement ps3 = null;
    private Connection connection = null;
    private String routingKey = null;

    public JS_CMCC_SynData(String routingKey) {
        this.routingKey = routingKey;
        initConnection();
    }

    @Override
    public SuperSynData withInit(String partner) {
        if (partner == null) throw new IllegalArgumentException("partner do not allow be null");
        this.partner = partner;
        return this;
    }

    @Override
    public void initConnection() {
        try {
            this.connection = JDBCPoolUtils.getConnection(this.routingKey);
            ps1 = this.connection.prepareStatement(sql1);
            ps2 = this.connection.prepareStatement(sql2);
            ps3 = this.connection.prepareStatement(sql3);
        } catch (Exception e) {
            logger.error("", partner, e);
        }
    }

    @Override
    public UserInfo getFromMysql(String mac) {
        UserInfo uif = null;
        try {
            if (connection == null || connection.isClosed()) {
                initConnection();
            }
            ps1.setString(1, mac);
            ResultSet executeQuery1 = ps1.executeQuery();
            if (executeQuery1.next()) {
                uif = new UserInfo();
                uif.setPartner(partner);
                uif.setProvince("江苏省");
                uif.setMac_addr(mac);//
                uif.setMain_account_id(executeQuery1.getString(1));//
                uif.setChild_account_id(executeQuery1.getString(1));//
                uif.setUser_id(executeQuery1.getString(1));//
                String user_city = executeQuery1.getString(2);// user_city
                ps2.setString(1, user_city);
                ResultSet executeQuery2 = ps2.executeQuery();
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
                ResultSet executeQuery3 = ps3.executeQuery();
                if (executeQuery3.next()) {
                    uif.setActivate_time(new Date(executeQuery3.getTimestamp(1).getTime()));
                }
                executeQuery3.close();
            } else {
                // 没有这条mac的纪录
                logger.warn("in[{}] mac-->{} is not found in mysql", partner, mac);
            }

        } catch (Exception e) {
            logger.error("", e);
        }
        return uif;
    }


}

package cn.gitv.bi.userinfo.rmconsumer.syndata;

import cn.gitv.bi.userinfo.rmconsumer.bean.UserInfo;
import cn.gitv.bi.userinfo.rmconsumer.utils.JDBCPoolUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

public class JS_CMCC_CP_SynData extends SuperSynData {
    private String partner = null;
    private static final String sql1 = "select USER_ID,CREATE_TIME from active_success where MAC=?;";
    private static final String sql2 = "select userCity,createTime,opType,idType from order_relation where phoneNumber=?;";
    private static final String sql3 = "select name from city where id=?;";
    private Connection connection = null;
    private String routingKey = null;
    private PreparedStatement ps1 = null;
    private PreparedStatement ps2 = null;
    private PreparedStatement ps3 = null;

    public JS_CMCC_CP_SynData(String routingKey) {
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
//				String MAC=mac.toUpperCase();//转成大写;
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
                uif.setActivate_time(new Date(executeQuery1.getTimestamp(2).getTime()));
                executeQuery1.close();
                ps2.setString(1, uif.getUser_id());
                ResultSet executeQuery2 = ps2.executeQuery();
                if (executeQuery2.next()) {
                    String userCity = executeQuery2.getString(1);
                    uif.setAccount_time(new Date(executeQuery2.getTimestamp(2).getTime()));
                    ps3.setString(1, userCity);
                    ResultSet executeQuery3 = ps3.executeQuery();
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

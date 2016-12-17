package cn.gitv.bi.userinfo.rmconsumer.syndata;

import cn.gitv.bi.userinfo.rmconsumer.bean.UserInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class AH_CMCC_SynData extends Super_SynData {
    private String partner = null;
    private static final String sql1 = "select MAIN_ACCOUNT_ID,CHILD_ACCOUNT_ID,USER_TYPE,CREATE_TIME from cont_user_mac where MAC_ADDR=?;";
    // cont_user_mac表的create_time是首次激活时间
    private static final String sql2 = "select AREA_NO,STATUS,CREATE_TIME from cont_user_child where USER_ID=?;";
    private static final String sql3 = "select AREA_NAME,CITY_NAME from cont_user_area_info where AREA_CODE=?;";

    @Override
    public Super_SynData withInit(String partner) {
        if (partner == null) throw new IllegalArgumentException("partner do not allow be null");
        this.partner = partner;
        return this;
    }

    @Override
    public List<UserInfo> getFromMysql(Set<String> mac_list, Connection safeConnection) {
        if (mac_list == null)
            return null;
        List<UserInfo> userinfos = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;
        try {
            userinfos = new ArrayList<UserInfo>();
            ps1 = safeConnection.prepareStatement(sql1);
            ps2 = safeConnection.prepareStatement(sql2);
            ps3 = safeConnection.prepareStatement(sql3);
            for (String mac : mac_list) {
                ps1.setString(1, mac);
                ResultSet executeQuery1 = ps1.executeQuery();
                if (executeQuery1.next()) {
                    UserInfo uif = new UserInfo();
                    uif.setPartner(partner);
                    uif.setProvince("安徽省");
                    uif.setMac_addr(mac);//
                    uif.setMain_account_id(executeQuery1.getString(1));//
                    uif.setChild_account_id(executeQuery1.getString(2));//
                    uif.setUser_id(executeQuery1.getString(2));//
                    uif.setUser_type(executeQuery1.getInt(3));// 用户类型(1:普客 2:集客)
                    uif.setActivate_time(new Date(executeQuery1.getTimestamp(4).getTime()));// 首次激活时间
                    ps2.setString(1, uif.getChild_account_id());//
                    ResultSet executeQuery2 = ps2.executeQuery();
                    if (executeQuery2.next()) {
                        String AREA_NO = executeQuery2.getString(1);
                        ps3.setString(1, AREA_NO);
                        ResultSet executeQuery3 = ps3.executeQuery();
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
                    executeQuery2.close();
                    userinfos.add(uif);
                } else {
                    // log 没有这条mac的纪录
                    logger.warn("in[ partner:{}]----> item:{} is not found", partner, mac);
                }
            }
            return userinfos;
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            try {
                if (ps3 != null)
                    ps3.close();
                if (ps2 != null)
                    ps2.close();
                if (ps1 != null)
                    ps1.close();
                if (safeConnection != null)
                    safeConnection.close();
            } catch (Exception e) {
                logger.error("", e);
            }
        }
        return null;
    }

}

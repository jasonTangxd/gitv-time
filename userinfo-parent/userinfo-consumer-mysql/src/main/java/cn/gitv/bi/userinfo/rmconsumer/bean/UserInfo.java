package cn.gitv.bi.userinfo.rmconsumer.bean;

import java.util.Date;

public class UserInfo {
    private String main_account_id;// 主账户
    private String child_account_id;// 从账户
    private String user_id;// 用户id
    private String mac_addr;// mac地址
    private String partner;// 合作伙伴
    private Date account_time;// 开户时间
    private Date activate_time;// 激活时间
    private int status = 0;// 状态
    private int user_type = 0;// 用户类型
    private String province;// 省份
    private String city_name;// 城市
    private String area_name;// 地区

    public String getMain_account_id() {
        return main_account_id;
    }

    public String getChild_account_id() {
        return child_account_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getMac_addr() {
        return mac_addr;
    }

    public String getPartner() {
        return partner;
    }

    public Date getAccount_time() {
        return account_time;
    }

    public Date getActivate_time() {
        return activate_time;
    }

    public int getStatus() {
        return status;
    }

    public int getUser_type() {
        return user_type;
    }

    public String getProvince() {
        return province;
    }

    public String getCity_name() {
        return city_name;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setMain_account_id(String main_account_id) {
        this.main_account_id = main_account_id;
    }

    public void setChild_account_id(String child_account_id) {
        this.child_account_id = child_account_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setMac_addr(String mac_addr) {
        this.mac_addr = mac_addr;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public void setAccount_time(Date account_time) {
        this.account_time = account_time;
    }

    public void setActivate_time(Date activate_time) {
        this.activate_time = activate_time;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    @Override
    public String toString() {
        return "UserInfo [main_account_id=" + main_account_id + ", child_account_id=" + child_account_id + ", user_id="
                + user_id + ", mac_addr=" + mac_addr + ", partner=" + partner + ", account_time=" + account_time
                + ", activate_time=" + activate_time + ", status=" + status + ", user_type=" + user_type + ", province="
                + province + ", city_name=" + city_name + ", area_name=" + area_name + "]";
    }
}

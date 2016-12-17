package cn.gitv.bi.launcher.beanparse.logbean;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.util.MultiMap;

public abstract class LogBean {
    private final static String mac_partner = "([A-Fa-f0-9]{2}:){5}[A-Fa-f0-9]{2}";
    /**
     * 以下属性是各个子类的共有字段
     */
    protected String Action;
    protected String MC;
    protected String P;
    protected String V;
    protected String UID;
    protected String STBID;
    protected String TS;
    protected String OS;
    protected String HM;
    protected String UG;
    protected String ip;
    protected String timestamp;
    protected String record_day;

    /**
     * 过滤条件：
     * 1.partner不为空
     * 2.mac地址的合法性
     */
    public boolean fieldsFilter() {
        boolean mac_flag = MC.matches(mac_partner);
        boolean partner_flag = StringUtils.isNotBlank(P);
        return mac_flag && partner_flag;
    }

    public abstract LogBean toLogBean(MultiMap<String> map, String ip, String timestamp);

    public abstract boolean stringToBeanInit(String content);

    public String getAction() {
        return Action;
    }

    public void setAction(String action) {
        Action = action;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp 格式化后赋值timestamp、record_day
     */

    public void setTimestamp(String timestamp) {
        String day = timestamp.substring(0, timestamp.indexOf("T"));
        String time = timestamp.substring(timestamp.indexOf("T") + 1, timestamp.indexOf("+"));
        this.timestamp = day + " " + time;
        this.record_day = day;
    }

    public String getRecord_day() {
        return record_day;
    }


}

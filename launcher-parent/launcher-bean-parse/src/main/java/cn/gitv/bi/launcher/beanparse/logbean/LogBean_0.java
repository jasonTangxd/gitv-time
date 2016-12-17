package cn.gitv.bi.launcher.beanparse.logbean;

import cn.gitv.bi.launcher.beanparse.utils.StringHandle;
import org.eclipse.jetty.util.MultiMap;

import java.util.List;
import java.util.Map;

import static cn.gitv.bi.launcher.beanparse.constant.Constant.LOGBEAN_SEPARATOR;
import static cn.gitv.bi.launcher.beanparse.constant.Constant.PT;


/**
 * 用户开机信息
 * "111.13.111.215|2016-11-13T17:51:36+08:00|/launcher_pb/v?A=100&" +
 * "HM=Q5&" +
 * "STBID=00420100060200601533002468C96917&" +
 * "UG=null&" +
 * "UID=00420100060200601533002468C96917&" +
 * "os=4.4.2&" +
 * "PV=null&" +
 * "V=0.0.2&" +
 * "P=ZJYD&" +
 * "UI=null&" +
 * "TS=20150101080041&" +
 * "re=1280*720&" +
 * "MC=00:24:68:c9:69:17|Dalvik/1.6.0 (Linux; U; Android 4.4.2; Q5 Build/KOT49H)";
 */
public class LogBean_0 extends LogBean {
    private String LIC;

    public void setLIC(String LIC) {
        this.LIC = LIC;
    }

    public void setP(String p) {
        this.P = p;
    }

    public void setMC(String MC) {
        this.MC = MC;
    }

    public void setV(String v) {
        V = v;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public void setSTBID(String STBID) {
        this.STBID = STBID;
    }

    public void setTS(String TS) {
        this.TS = TS;
    }

    public void setOS(String OS) {
        this.OS = OS;
    }

    public void setHM(String HM) {
        this.HM = HM;
    }

    public void setUG(String UG) {
        this.UG = UG;
    }


    @Override
    public LogBean toLogBean(MultiMap<String> map, String ip, String timestamp) {
        this.setIp(ip);
        this.setTimestamp(timestamp);
        for (Map.Entry<String, List<String>> item : map.entrySet()) {
            String key = item.getKey();
            String value = StringHandle.getPrintStringAndReplaceAll(item.getValue().get(0), PT, "");
            switch (key) {
                case "A":
                    this.setAction(value);
                    break;
                case "P":
                    this.setP(value);
                    break;
                case "MC":
                    this.setMC(value);
                    break;
                case "V":
                    this.setV(value);
                    break;
                case "UID":
                    this.setUID(value);
                    break;
                case "STBID":
                    this.setSTBID(value);
                    break;
                case "TS":
                    this.setTS(value);
                    break;
                case "os":
                    this.setOS(value);
                    break;
                case "HM":
                    this.setHM(value);
                    break;
                case "UG":
                    this.setUG(value);
                    break;
                case "LIC":
                    this.setLIC(value);
                    break;
                default:
                    break;
            }
        }
        return this;
    }

    private void init(List<String> ls) {
        this.Action = ls.get(0);
        this.P = ls.get(1);
        this.MC = ls.get(2);
        this.V = ls.get(3);
        this.UID = ls.get(4);
        this.STBID = ls.get(5);
        this.TS = ls.get(6);
        this.OS = ls.get(7);
        this.HM = ls.get(8);
        this.UG = ls.get(9);
        this.LIC = ls.get(10);
        this.ip = ls.get(11);
        this.timestamp = ls.get(12);
        this.record_day = ls.get(13);
    }

    @Override
    public boolean stringToBeanInit(String content) {
        List<String> contents = StringHandle.str_split(content);
        if (contents.size() == 14) {
            this.init(contents);
            return true;
        } else {
            return false;
        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Action).append(LOGBEAN_SEPARATOR).append(P).append(LOGBEAN_SEPARATOR).append(MC).append(LOGBEAN_SEPARATOR).append(V).append(LOGBEAN_SEPARATOR)
                .append(UID).append(LOGBEAN_SEPARATOR).append(STBID).append(LOGBEAN_SEPARATOR).append(TS).append(LOGBEAN_SEPARATOR).append(OS).append(LOGBEAN_SEPARATOR)
                .append(HM).append(LOGBEAN_SEPARATOR).append(UG).append(LOGBEAN_SEPARATOR).append(LIC).append(LOGBEAN_SEPARATOR).append(ip).append(LOGBEAN_SEPARATOR)
                .append(timestamp).append(LOGBEAN_SEPARATOR).append(record_day);
        return sb.toString();
    }
}

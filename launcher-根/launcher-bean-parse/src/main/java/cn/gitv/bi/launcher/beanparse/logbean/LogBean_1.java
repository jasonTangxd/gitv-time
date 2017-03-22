package cn.gitv.bi.launcher.beanparse.logbean;

import cn.gitv.bi.launcher.beanparse.constant.Constant;
import cn.gitv.bi.launcher.beanparse.utils.StringHandle;
import org.eclipse.jetty.util.MultiMap;

import java.util.List;
import java.util.Map.Entry;

import static cn.gitv.bi.launcher.beanparse.constant.Constant.PT;

/**
 * Created by Kang on 2016/10/22.
 */

/**
 * 218.241.193.75|2016-11-18T17:51:03+08:00|/launcher_pb/v?
 * A=101&
 * HM=unknown&
 * OV=20.00.00.01&
 * STBID=004001FF003182A001E5000763F561F0&
 * UG=3098&
 * UID=54002000020&
 * os=4.4.2&
 * PV=null&
 * V=20.00.00.02&
 * P=JS_CMCC_CP&
 * UI=null&
 * TS=20161118175102&
 * re=1280*720&
 * MC=00:07:63:f5:61:f0|Dalvik/1.6.0 (Linux; U; Android 4.4.2; unknown Build/M1518H)
 */
public class LogBean_1 extends LogBean {
    private String OV;


    public void setP(String p) {
        P = p;
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

    public void setOV(String OV) {
        this.OV = OV;
    }

    public void setUG(String UG) {
        this.UG = UG;
    }

    @Override
    public LogBean toLogBean(MultiMap<String> map, String ip, String timestamp) {
        this.setIp(ip);
        this.setTimestamp(timestamp);
        for (Entry<String, List<String>> item : map.entrySet()) {
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
                case "OV":
                    this.setOV(value);
                    break;
                case "UG":
                    this.setUG(value);
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
        this.OV = ls.get(9);
        this.UG = ls.get(10);
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
        final StringBuffer sb = new StringBuffer();
        sb.append(Action).append(Constant.LOGBEAN_SEPARATOR).append(P).append(Constant.LOGBEAN_SEPARATOR).append(MC).append(Constant.LOGBEAN_SEPARATOR).append(V)
                .append(Constant.LOGBEAN_SEPARATOR).append(UID).append(Constant.LOGBEAN_SEPARATOR).append(STBID).append(Constant.LOGBEAN_SEPARATOR).append(TS)
                .append(Constant.LOGBEAN_SEPARATOR).append(OS).append(Constant.LOGBEAN_SEPARATOR).append(HM).append(Constant.LOGBEAN_SEPARATOR).append(OV)
                .append(Constant.LOGBEAN_SEPARATOR).append(UG).append(Constant.LOGBEAN_SEPARATOR).append(ip).append(Constant.LOGBEAN_SEPARATOR)
                .append(timestamp).append(Constant.LOGBEAN_SEPARATOR).append(record_day);
        return sb.toString();
    }

}

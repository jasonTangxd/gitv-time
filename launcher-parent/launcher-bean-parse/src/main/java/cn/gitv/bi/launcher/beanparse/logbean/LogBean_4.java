package cn.gitv.bi.launcher.beanparse.logbean;

import cn.gitv.bi.launcher.beanparse.constant.Constant;
import cn.gitv.bi.launcher.beanparse.utils.StringHandle;
import org.eclipse.jetty.util.MultiMap;

import java.util.List;
import java.util.Map.Entry;

import static cn.gitv.bi.launcher.beanparse.constant.Constant.LOGBEAN_SEPARATOR;
import static cn.gitv.bi.launcher.beanparse.constant.Constant.PT;

/**
 * Created by Kang on 2016/10/22.
 */

/**
 * launcher_pb/v?A=104&
 * HM=iS-E5-NGH&
 * OTN=YINHE_SXYD_LNB&
 * STBID=000000000000000000000CC655A834EE&
 * TNUM=SNTY&
 * UG=null&
 * UID=20160830ygyh02&
 * os=4.4.2&
 * PV=null&V=1.0.20&
 * P=SAXYD&
 * UI=null&
 * TS=20161120151353&
 * re=1280*720&
 * MC=0c:c6:55:a8:34:ee
 */
public class LogBean_4 extends LogBean {
    private String TNUM;
    private String OTN;

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

    public void setTNUM(String TNUM) {
        this.TNUM = TNUM;
    }

    public void setOTN(String OTN) {
        this.OTN = OTN;
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
                case "TNUM":
                    this.setTNUM(value);
                    break;
                case "OTN":
                    this.setOTN(value);
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

    @Override
    public boolean stringToBeanInit(String content) {
        List<String> contents = StringHandle.str_split(content);
        if (contents.size() == 15) {
            this.init(contents);
            return true;
        } else {
            return false;
        }
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
        this.TNUM = ls.get(9);
        this.OTN = ls.get(10);
        this.UG = ls.get(11);
        this.ip = ls.get(12);
        this.timestamp = ls.get(13);
        this.record_day = ls.get(14);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append(Action).append(LOGBEAN_SEPARATOR).append(P).append(LOGBEAN_SEPARATOR).append(MC).append(LOGBEAN_SEPARATOR).append(V).append(LOGBEAN_SEPARATOR)
                .append(UID).append(LOGBEAN_SEPARATOR).append(STBID).append(LOGBEAN_SEPARATOR).append(TS).append(LOGBEAN_SEPARATOR)
                .append(OS).append(LOGBEAN_SEPARATOR).append(HM).append(LOGBEAN_SEPARATOR).append(TNUM).append(LOGBEAN_SEPARATOR)
                .append(OTN).append(LOGBEAN_SEPARATOR).append(UG).append(LOGBEAN_SEPARATOR).append(ip).append(LOGBEAN_SEPARATOR)
                .append(timestamp).append(LOGBEAN_SEPARATOR).append(record_day);
        return sb.toString();
    }
}

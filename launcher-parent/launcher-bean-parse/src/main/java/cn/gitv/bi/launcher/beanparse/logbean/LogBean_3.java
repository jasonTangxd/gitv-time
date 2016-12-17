package cn.gitv.bi.launcher.beanparse.logbean;

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
 * /launcher_pb/v?A=103&
 * APK={\x22mComponent\x22:{\x22mClass\x22:\x22com.gitv.tv.launcher.activity.WelcomeActivity\x22,\x22mPackage\x22:\x22com.gitv.tv.launcher\x22},\x22mFlags\x22:0}&
 * HM=iS-E5-NGH&
 * RE=0&
 * STBID=000000000000000000000CC655A834EE&
 * TP=1&
 * UG=null&
 * UID=20160830ygyh02&
 * os=4.4.2&
 * PV=null&
 * V=1.0.20&
 * P=SAXYD&
 * UI=null&
 * TS=20161115110654&
 * re=1280*720&
 * MC=0c:c6:55:a8:34:ee
 */
public class LogBean_3 extends LogBean {
    private String APK;
    private String TP;
    private String RE;


    public String getAPK() {
        return APK;
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
                case "APK":
                    this.setAPK(value);
                    break;
                case "TP":
                    this.setTP(value);
                    break;
                case "RE":
                    this.setRE(value);
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

    public void setAPK(String APK) {
        this.APK = APK;
    }

    public void setTP(String TP) {
        this.TP = TP;
    }

    public void setRE(String RE) {
        this.RE = RE;
    }

    public void setUG(String UG) {
        this.UG = UG;
    }

    @Override
    public boolean stringToBeanInit(String content) {
        List<String> contents = StringHandle.str_split(content);
        if (contents.size() == 16) {
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
        this.APK = ls.get(9);
        this.TP = ls.get(10);
        this.RE = ls.get(11);
        this.UG = ls.get(12);
        this.ip = ls.get(13);
        this.timestamp = ls.get(14);
        this.record_day = ls.get(15);
    }

    /**
     * final string to bean
     */
    public boolean string4ConApkToBeanInit(String content) {
        List<String> contents = StringHandle.str_split(content);
        if (contents.size() == 33) {
            this.init4ConApk(contents);
            return true;
        } else {
            return false;
        }
    }

    private void init4ConApk(List<String> ls) {
        this.Action = ls.get(0);
        this.P = ls.get(1);
        this.MC = ls.get(2);
        this.V = ls.get(3);
        this.UID = ls.get(4);
        this.STBID = ls.get(5);
        this.TS = ls.get(6);
        this.OS = ls.get(7);
        this.HM = ls.get(8);
// apk解析后占19个字段
        this.TP = ls.get(27);
        this.RE = ls.get(28);
        this.UG = ls.get(29);
        this.ip = ls.get(30);
        this.timestamp = ls.get(31);
        this.record_day = ls.get(32);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(Action).append(LOGBEAN_SEPARATOR).append(P).append(LOGBEAN_SEPARATOR).append(MC).append(LOGBEAN_SEPARATOR).append(V).append(LOGBEAN_SEPARATOR).append(UID).append(LOGBEAN_SEPARATOR)
                .append(STBID).append(LOGBEAN_SEPARATOR).append(TS).append(LOGBEAN_SEPARATOR).append(OS).append(LOGBEAN_SEPARATOR).append(HM).append(LOGBEAN_SEPARATOR).append(APK).append(LOGBEAN_SEPARATOR)
                .append(TP).append(LOGBEAN_SEPARATOR).append(RE).append(LOGBEAN_SEPARATOR).append(UG).append(LOGBEAN_SEPARATOR).append(ip).append(LOGBEAN_SEPARATOR)
                .append(timestamp).append(LOGBEAN_SEPARATOR).append(record_day);
        return sb.toString();
    }

    /**
     * and apk parse -->string
     */
    public String toString(String APK_Trans) {
        final StringBuilder sb = new StringBuilder();
        sb.append(Action).append(LOGBEAN_SEPARATOR).append(P).append(LOGBEAN_SEPARATOR).append(MC).append(LOGBEAN_SEPARATOR).append(V).append(LOGBEAN_SEPARATOR).append(UID).append(LOGBEAN_SEPARATOR)
                .append(STBID).append(LOGBEAN_SEPARATOR).append(TS).append(LOGBEAN_SEPARATOR).append(OS).append(LOGBEAN_SEPARATOR).append(HM).append(LOGBEAN_SEPARATOR).append(APK_Trans).append(LOGBEAN_SEPARATOR)
                .append(TP).append(LOGBEAN_SEPARATOR).append(RE).append(LOGBEAN_SEPARATOR).append(UG).append(LOGBEAN_SEPARATOR).append(ip).append(LOGBEAN_SEPARATOR)
                .append(timestamp).append(LOGBEAN_SEPARATOR).append(record_day);
        return sb.toString();
    }
}

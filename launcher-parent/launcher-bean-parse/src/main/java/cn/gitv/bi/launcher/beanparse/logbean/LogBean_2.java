package cn.gitv.bi.launcher.beanparse.logbean;

import cn.gitv.bi.launcher.beanparse.utils.StringHandle;
import org.eclipse.jetty.util.MultiMap;

import java.util.List;
import java.util.Map.Entry;

import static cn.gitv.bi.launcher.beanparse.constant.Constant.LOGBEAN_SEPARATOR;
import static cn.gitv.bi.launcher.beanparse.constant.Constant.PT;


/**
 * /launcher_pb/v?
 * A=102&
 * APK={\x22mComponent\x22:{\x22mClass\x22:\x22com.gitv.tv.live.activity.VodActivity\x22,\x22mPackage\x22:\x22com.gitv.tv.live\x22},\x22mFlags\x"22:268468224}&
 * AR=2&
 * HM=Q5&
 * SID=188&
 * STBID=00420100060200601533002468C96917&
 * TNUM=null&UG=null&
 * UID=00420100060200601533002468C96917&
 * X=0&
 * Y=0&
 * os=4.4.2&
 * PV=null&
 * V=0.0.2&
 * P=ZJYD&
 * UI=null&
 * TS=20150101080045&
 * re=1280*720
 * &MC=00:24:68:c9:69:17
 */
public class LogBean_2 extends LogBean {
    private String APK;
    private String SID;
    private String AR;
    private String X;
    private String Y;
    private String TNUM;
    private String CTP;

    public void setCTP(String CTP) {
        this.CTP = CTP;
    }

    public String getAPK() {
        return APK;
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

    public void setAPK(String APK) {
        this.APK = APK;
    }

    public void setSID(String SID) {
        this.SID = SID;
    }

    public void setAR(String AR) {
        this.AR = AR;
    }

    public void setX(String x) {
        X = x;
    }

    public void setY(String y) {
        Y = y;
    }

    public void setTNUM(String TNUM) {
        this.TNUM = TNUM;
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
                case "APK":
                    this.setAPK(value);
                    break;
                case "SID":
                    this.setSID(value);
                    break;
                case "AR":
                    this.setAR(value);
                    break;
                case "X":
                    this.setX(value);
                    break;
                case "Y":
                    this.setY(value);
                    break;
                case "TNUM":
                    this.setTNUM(value);
                    break;
                case "UG":
                    this.setUG(value);
                    break;
                case "CTP":
                    this.setCTP(value);
                default:
                    break;
            }
        }
        return this;
    }

    /**
     * string to bean
     */
    @Override
    public boolean stringToBeanInit(String content) {
        List<String> contents = StringHandle.str_split(content);
        if (contents.size() == 20) {
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
        this.SID = ls.get(10);
        this.AR = ls.get(11);
        this.X = ls.get(12);
        this.Y = ls.get(13);
        this.TNUM = ls.get(14);
        this.UG = ls.get(15);
        this.CTP = ls.get(16);
        this.ip = ls.get(17);
        this.timestamp = ls.get(18);
        this.record_day = ls.get(19);
    }

    /**
     * 用来将完全解析包含解析后json APK完的a=102的string -> bean模式
     */
    public boolean string4ConApkToBeanInit(String content) {
        List<String> contents = StringHandle.str_split(content);
        if (contents.size() == 37) {
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
        //apk解析后占19个字段
        this.SID = ls.get(27);
        this.AR = ls.get(28);
        this.X = ls.get(29);
        this.Y = ls.get(30);
        this.TNUM = ls.get(31);
        this.UG = ls.get(32);
        this.CTP = ls.get(33);
        this.ip = ls.get(34);
        this.timestamp = ls.get(35);
        this.record_day = ls.get(36);
    }

    /**
     * Apk[json]未解析to string
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Action).append(LOGBEAN_SEPARATOR).append(P).append(LOGBEAN_SEPARATOR).append(MC).append(LOGBEAN_SEPARATOR).append(V).append(LOGBEAN_SEPARATOR).append(UID).append(LOGBEAN_SEPARATOR)
                .append(STBID).append(LOGBEAN_SEPARATOR).append(TS).append(LOGBEAN_SEPARATOR).append(OS).append(LOGBEAN_SEPARATOR).append(HM).append(LOGBEAN_SEPARATOR)
                .append(APK).append(LOGBEAN_SEPARATOR).append(SID).append(LOGBEAN_SEPARATOR).append(AR).append(LOGBEAN_SEPARATOR).append(X).append(LOGBEAN_SEPARATOR)
                .append(Y).append(LOGBEAN_SEPARATOR).append(TNUM).append(LOGBEAN_SEPARATOR).append(UG).append(LOGBEAN_SEPARATOR).append(CTP).append(LOGBEAN_SEPARATOR)
                .append(ip).append(LOGBEAN_SEPARATOR).append(timestamp).append(LOGBEAN_SEPARATOR).append(record_day);
        return sb.toString();
    }

    /**
     * 解析APK后 to string
     */

    public String toString(String APK_Trans) {
        final StringBuilder sb = new StringBuilder();
        sb.append(Action).append(LOGBEAN_SEPARATOR).append(P).append(LOGBEAN_SEPARATOR).append(MC).append(LOGBEAN_SEPARATOR).append(V).append(LOGBEAN_SEPARATOR).append(UID).append(LOGBEAN_SEPARATOR)
                .append(STBID).append(LOGBEAN_SEPARATOR).append(TS).append(LOGBEAN_SEPARATOR).append(OS).append(LOGBEAN_SEPARATOR).append(HM).append(LOGBEAN_SEPARATOR)
                .append(APK_Trans).append(LOGBEAN_SEPARATOR).append(SID).append(LOGBEAN_SEPARATOR).append(AR).append(LOGBEAN_SEPARATOR).append(X).append(LOGBEAN_SEPARATOR)
                .append(Y).append(LOGBEAN_SEPARATOR).append(TNUM).append(LOGBEAN_SEPARATOR).append(UG).append(LOGBEAN_SEPARATOR).append(CTP).append(LOGBEAN_SEPARATOR)
                .append(ip).append(LOGBEAN_SEPARATOR).append(timestamp).append(LOGBEAN_SEPARATOR).append(record_day);
        return sb.toString();
    }
}

package cn.gitv.bi.launcher.logclean.datatest;

import cn.gitv.bi.launcher.beanparse.apkparse.ApkBean;
import cn.gitv.bi.launcher.beanparse.apkparse.ApkParseUtils;
import cn.gitv.bi.launcher.beanparse.logbean.LogBean;
import cn.gitv.bi.launcher.beanparse.logbean.LogBean_2;
import cn.gitv.bi.launcher.logclean.mapper.FieldMapToBean;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by Kang on 2016/11/14.
 */
public class KafkaDateTest {
    String data_100 = "/launcher_pb/v?A=100&" +
            "HM=Q5&" +
            "STBID=00420100060200601533002468C96917&" +
            "UG=null&" +
            "UID=00420100060200601533002468C96917&" +
            "os=4.4.2&" +
            "PV=null&" +
            "V=0.0.2&" +
            "P=ZJYD&" +
            "UI=null&" +
            "TS=20150101080041&" +
            "re=1280*720&" +
            "MC=00:24:68:c9:69:17";
    String data_101 = "/launcher_pb/v?A=101&" +
            "HM=unknown&" +
            "OV=20.00.00.01&" +
            "STBID=004001FF003182A001E5000763F561F0&" +
            "UG=3098&" +
            "UID=54002000020&" +
            "os=4.4.2&" +
            "PV=null&" +
            "V=20.00.00.02&" +
            "P=JS_CMCC_CP&" +
            "UI=null&" +
            "TS=20161118175102&" +
            "re=1280*720&" +
            "MC=00:07:63:f5:61:f0";
    String data_102 = "/launcher_pb/v?" +
            "A=102&" +
            "APK={\\x22mComponent\\x22:{\\x22mClass\\x22:\\x22com.gitv.tv.live.activity.VodActivity\\x22,\\x22mPackage\\x22:\\x22com.gitv.tv.live\\x22},\\x22mFlags\\x\22:268468224}&" +
            "AR=2&" +
            "HM=Q5&" +
            "SID=188&" +
            "STBID=00420100060200601533002468C96917&" +
            "TNUM=null&" +
            "UG=null&" +
            "UID=00420100060200601533002468C96917&" +
            "X=0&Y=0&" +
            "os=4.4.2&PV=null&" +
            "V=0.0.2&" +
            "P=ZJYD&" +
            "UI=null&" +
            "TS=20150101080045&" +
            "re=1280*720&" +
            "MC=00:24:68:c9:69:17";
    String data_103 = "/launcher_pb/v?" +
            "A=103&" +
            "APK={\\x22mComponent\\x22:{\\x22mClass\\x22:\\x22com.gitv.tv.launcher.activity.WelcomeActivity\\x22,\\x22mPackage\\x22:\\x22com.gitv.tv.launcher\\x22},\\x22mFlags\\x22:0}&" +
            "HM=iS-E5-NGH&" +
            "RE=0&" +
            "STBID=000000000000000000000CC655A834EE&" +
            "TP=1&" +
            "UG=null&" +
            "UID=20160830ygyh02&" +
            "os=4.4.2&" +
            "PV=null&" +
            "V=1.0.20&" +
            "P=SAXYD&" +
            "UI=null&" +
            "TS=20161115110654&" +
            "re=1280*720&" +
            "MC=0c:c6:55:a8:34:ee";
    String data_104 = "/launcher_pb/v?" +
            "A=104&" +
            "HM=iS-E5-NGH&" +
            "OTN=null&" +
            "STBID=004803FF00196800160420896FF94A2D&" +
            "TNUM=YINHE_SXYD_LNB&" +
            "UG=null&UID=13909221889&" +
            "os=4.4.2&" +
            "PV=null&" +
            "V=1.0.20&" +
            "P=SAXYD&" +
            "UI=null&" +
            "TS=20161118171815&" +
            "re=1280*720&" +
            "MC=20:89:6f:f9:4a:2d";

    String full_data = "/launcher_pb/v?A=102&APK=%7B%27mComponent%27%3A%7B%27mClass%27%3A%27com.galaxyitv.video.ui.JumpActivity%27%2C%27mPackage%27%3A%27com.galaxyitv.video%27%7D%2C%27mExtras%27%3A%7B%27mClassLoader%27%3A%7B%27packages%27%3A%7B%27com.android.org.conscrypt%27%3A%7B%27implTitle%27%3A%27Unknown%27%2C%27implVendor%27%3A%27Unknown%27%2C%27implVersion%27%3A%270.0%27%2C%27name%27%3A%27com.android.org.conscrypt%27%2C%27specTitle%27%3A%27Unknown%27%2C%27specVendor%27%3A%27Unknown%27%2C%27specVersion%27%3A%270.0%27%7D%7D%7D%2C%27mMap%27%3A%7B%27type%27%3A%276%27%7D%2C%27mHasFds%27%3Afalse%2C%27mFdsKnown%27%3Atrue%2C%27mAllowFds%27%3Afalse%7D%2C%27mFlags%27%3A268468224%7D&AR=2&CTP=%E7%94%B5%E8%A7%86%E5%89%A7&HM=unknown&SID=530&STBID=004001FF003182A001E5000763F561F0&TNUM=3099&UG=3099&UID=54002000021&X=6&Y=4&os=4.4.2&PV=null&V=20.00.00.08&P=JS_CMCC_CP&UI=null&TS=20161206144121&re=1280*720&MC=00:07:63:f5:61:f0";

    public String nginxGet() {
        String ip = "10.10.121.112";
        String time = "2016-10-08T17:52:29+08:00";
        String url_analyze = full_data.substring(full_data.indexOf("?") + 1, full_data.length());
        MultiMap<String> values = new MultiMap<String>();
        UrlEncoded.decodeTo(url_analyze, values, "utf-8", 1000);
        LogBean lb = FieldMapToBean.getLogBean(values, ip, time);
        String launcher_log = lb.toString();
        return launcher_log;
    }

    @Test
    public void apkParse() throws IOException {
        LogBean_2 logbean2 = new LogBean_2();
        logbean2.stringToBeanInit(nginxGet());
        String APK = logbean2.getAPK();
        String json = APK.replaceAll("'", "\"");
        System.out.println(json);
        ApkBean ab = ApkParseUtils.jsonToBean(json);
        String final_data=logbean2.toString(ab.toString());
        System.out.println(final_data);
    }
}
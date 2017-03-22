package cn.gitv.bi.launcher.logclean.mapper;
import cn.gitv.bi.launcher.beanparse.logbean.*;
import org.eclipse.jetty.util.MultiMap;

public class FieldMapToBean {
    public static LogBean getLogBean(MultiMap<String> map, String ip, String timestamp) {
        String Action = map.getValue("A", 0);
        switch (Action) {
            case "100"://successed
                LogBean_0 logbean0 = new LogBean_0();
                return logbean0.toLogBean(map, ip, timestamp);
            case "101"://successed
                LogBean_1 logbean1 = new LogBean_1();
                return logbean1.toLogBean(map, ip, timestamp);
            case "102"://successed
                LogBean_2 logbean2 = new LogBean_2();
                return logbean2.toLogBean(map, ip, timestamp);
            case "103"://successed
                LogBean_3 logbean3 = new LogBean_3();
                return logbean3.toLogBean(map, ip, timestamp);
            case "104"://successed
                LogBean_4 logbean4 = new LogBean_4();
                return logbean4.toLogBean(map, ip, timestamp);
            default:
                return null;
        }
    }
}

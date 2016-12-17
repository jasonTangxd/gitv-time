package cn.gitv.bi.userinfo.rmconsumer.syndata;

import java.util.HashMap;
import java.util.Map;

import static cn.gitv.bi.userinfo.rmconsumer.constant.Constant.*;

public class SynFactory {
    public static Map<String, Super_SynData> factory = new HashMap<String, Super_SynData>();

    static {
        factory.put(AH_CMCC, new AH_CMCC_SynData());
        factory.put(JS_CMCC, new JS_CMCC_SynData());
        factory.put(JS_CMCC_CP, new JS_CMCC_CP_SynData());
    }

    public static Super_SynData getSynInstance(String routingKey) {
        Super_SynData synData = factory.get(routingKey);
        if (synData == null) {
            throw new IllegalArgumentException("no this Partner imp!");
        }
        return synData;
    }
}

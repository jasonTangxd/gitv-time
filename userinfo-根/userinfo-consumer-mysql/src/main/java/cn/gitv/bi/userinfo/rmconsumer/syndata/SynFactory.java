package cn.gitv.bi.userinfo.rmconsumer.syndata;

import java.util.HashMap;
import java.util.Map;

import static cn.gitv.bi.userinfo.rmconsumer.constant.Constant.*;

public class SynFactory {
    public static Map<String, SuperSynData> factory = new HashMap<String, SuperSynData>();

    static {
        factory.put(AH_CMCC, new AH_CMCC_SynData(AH_CMCC));
        factory.put(JS_CMCC, new JS_CMCC_SynData(JS_CMCC));
        factory.put(JS_CMCC_CP, new JS_CMCC_CP_SynData(JS_CMCC_CP));
    }

    public static SuperSynData getSynInstance(String routingKey) {
        SuperSynData synData = factory.get(routingKey);
        if (synData == null) {
            throw new IllegalArgumentException("no this Partner imp!");
        }
        return synData;
    }
}

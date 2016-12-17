package cn.gitv.bi.external.netty4rpc.services.impl;

import cn.gitv.bi.external.netty4rpc.services.AddCalculate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddCalculateImpl implements AddCalculate {
    private static Logger LOG = LoggerFactory.getLogger(AddCalculateImpl.class);

    //两数相加
    public int add(int a, int b) {
        LOG.info("server is called and res:{}", a + b);
        return a + b;
    }
}

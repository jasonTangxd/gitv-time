package cn.gitv.bi.external.netty4rpc.tpooltest;

import cn.gitv.bi.external.netty4rpc.netty.clientimp.MessageSendExecutor;
import cn.gitv.bi.external.netty4rpc.services.AddCalculate;
import cn.gitv.bi.external.netty4rpc.thpool.customized.RpcCustomizedThreadPool;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.Executor;

/**
 * Created by Kang on 2016/12/11.
 */
public class ThreadPoolTest {
    @Test
    public void clientStart() {
        MessageSendExecutor messageSendExecutor = new MessageSendExecutor("127.0.0.1:18888");
        AddCalculate addCalculate = messageSendExecutor.execute(AddCalculate.class);
        System.out.println(addCalculate.add(10, 100));
    }

    @Test
    public void getJMXPool() {
        Executor customizedExecutorWithJMX = RpcCustomizedThreadPool.getCustomizedExecutorWithJMX(100, 10);
        for (int i = 0; i < 10000; i++) {
            customizedExecutorWithJMX.execute(new Runnable() {
                @Override
                public void run() {
                    new Random(1000).nextLong();
                }
            });
        }
    }
}

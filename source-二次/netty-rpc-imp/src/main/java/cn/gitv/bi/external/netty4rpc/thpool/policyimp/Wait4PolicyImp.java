package cn.gitv.bi.external.netty4rpc.thpool.policyimp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class Wait4PolicyImp implements RejectedExecutionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(Wait4PolicyImp.class);

    private String threadName;

    public Wait4PolicyImp() {
        this(null);
    }

    public Wait4PolicyImp(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
        if (threadName != null) {
            LOG.error("RPC Thread pool [{}] is exhausted, executor={}", threadName, executor.toString());
        }

        if (!executor.isShutdown()) {
            try {
                executor.getQueue().put(runnable);
                //waiting if necessary:put 是阻塞式的
            } catch (InterruptedException e) {
                LOG.error("", e);
            }
        }
    }
}


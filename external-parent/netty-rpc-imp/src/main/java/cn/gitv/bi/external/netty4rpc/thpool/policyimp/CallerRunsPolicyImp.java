package cn.gitv.bi.external.netty4rpc.thpool.policyimp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadPoolExecutor;

/*线程调用运行该任务的execute本身.此策略提供简单的反馈控制机制,能够减缓新任务的提交速度*/
public class CallerRunsPolicyImp extends ThreadPoolExecutor.CallerRunsPolicy {
    private static final Logger LOG = LoggerFactory.getLogger(CallerRunsPolicyImp.class);

    private String threadName;

    public CallerRunsPolicyImp() {
        this(null);
    }

    public CallerRunsPolicyImp(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public void rejectedExecution(Runnable task, ThreadPoolExecutor executor) {
        if (threadName != null) {
            LOG.error("RPC Thread pool [{}] is exhausted, executor={}", threadName, executor.toString());
        }
        super.rejectedExecution(task, executor);
    }
}


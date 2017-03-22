package cn.gitv.bi.external.netty4rpc.thpool.policyimp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadPoolExecutor;

/*默认的 ThreadPoolExecutor.AbortPolicyImp 中,处理程序遭到拒绝将抛出运行时 RejectedExecutionException*/
public class AbortPolicyImp extends ThreadPoolExecutor.AbortPolicy {
    private static final Logger LOG = LoggerFactory.getLogger(AbortPolicyImp.class);

    private String threadName;

    public AbortPolicyImp() {
        this(null);
    }

    public AbortPolicyImp(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public void rejectedExecution(Runnable task, ThreadPoolExecutor executor) {
        if (threadName != null) {
            LOG.error("RPC Thread pool [{}] is exhausted, executor={}", threadName, executor.toString());
        }
        String msg = String.format("RpcServer["
                        + " Thread Name: %s, Pool Size: %d (active: %d, core: %d, max: %d, largest: %d), Task: %d (completed: %d),"
                        + " Executor status:(isShutdown:%s, isTerminated:%s, isTerminating:%s)]",
                threadName, executor.getPoolSize(), executor.getActiveCount(), executor.getCorePoolSize(), executor.getMaximumPoolSize(), executor.getLargestPoolSize(),
                executor.getTaskCount(), executor.getCompletedTaskCount(), executor.isShutdown(), executor.isTerminated(), executor.isTerminating());
        LOG.warn("rejectedExecution msg:", msg);
        super.rejectedExecution(task, executor);
    }
}


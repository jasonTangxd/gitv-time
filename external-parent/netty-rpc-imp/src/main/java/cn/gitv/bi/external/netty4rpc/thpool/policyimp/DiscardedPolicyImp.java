package cn.gitv.bi.external.netty4rpc.thpool.policyimp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/*不能执行的任务将被删除*/
public class DiscardedPolicyImp implements RejectedExecutionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(DiscardedPolicyImp.class);

    private String threadName;

    public DiscardedPolicyImp() {
        this(null);
    }

    public DiscardedPolicyImp(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
        if (threadName != null) {
            LOG.error("RPC Thread pool [{}] is exhausted, executor={}", threadName, executor.toString());
        }

        if (!executor.isShutdown()) {
            BlockingQueue<Runnable> queue = executor.getQueue();
            int discardSize = queue.size() >> 1;
            for (int i = 0; i < discardSize; i++) {
                queue.poll();
                //从队列中删除第一个元素
            }
            queue.offer(runnable);
            //不阻塞：可添加返回true,不可false
        }
    }
}

package cn.gitv.bi.external.netty4rpc.thpool.policyimp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/*送走一个购票者,等待售票者可服务时,下一个购票者方可使用*/
public class BuyTicketPolicyImp implements RejectedExecutionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(BuyTicketPolicyImp.class);

    private final String threadName;

    public BuyTicketPolicyImp() {
        this(null);
    }

    public BuyTicketPolicyImp(String threadName) {
        this.threadName = threadName;
    }

    public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
        if (threadName != null) {
            LOG.error("RPC Thread pool [{}] is exhausted, executor={}", threadName, executor.toString());
        }
        if (runnable instanceof RejectedRunnable) {
            ((RejectedRunnable) runnable).rejected();
        } else {
            if (!executor.isShutdown()) {
                BlockingQueue<Runnable> queue = executor.getQueue();
                int discardSize = queue.size() >> 1;
                for (int i = 0; i < discardSize; i++) {
                    queue.poll();
                }
                //从队列中删除第一个元素
                try {
                    queue.put(runnable);
                    //阻塞到可以添加为止
                } catch (InterruptedException e) {
                }
            }
        }
    }
}


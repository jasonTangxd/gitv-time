package cn.gitv.bi.external.netty4rpc.thpool.customized;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;

import cn.gitv.bi.external.netty4rpc.core.RpcSystemConfig;
import cn.gitv.bi.external.netty4rpc.threadpool.RejectedPolicyType;
import cn.gitv.bi.external.netty4rpc.threadpool.WorkQueueType;
import cn.gitv.bi.external.netty4rpc.thpool.jmxstatus.ThreadPoolMonitorProvider;
import cn.gitv.bi.external.netty4rpc.thpool.jmxstatus.ThreadPoolStatus;
import cn.gitv.bi.external.netty4rpc.thpool.policyimp.AbortPolicyImp;
import cn.gitv.bi.external.netty4rpc.thpool.policyimp.BuyTicketPolicyImp;
import cn.gitv.bi.external.netty4rpc.thpool.policyimp.CallerRunsPolicyImp;
import cn.gitv.bi.external.netty4rpc.thpool.policyimp.DiscardedPolicyImp;
import cn.gitv.bi.external.netty4rpc.thpool.policyimp.Wait4PolicyImp;

/*rpc线程池封装*/
public class RpcCustomizedThreadPool {
    private static final Timer timer = new Timer("ThreadPoolMonitor", true);
    private static long monitorDelay = 100;
    private static long monitorPeriod = 300;
    private static final Logger LOG = LoggerFactory.getLogger(RpcCustomizedThreadPool.class);

    /*线程池所使用的缓冲队列*/
    private static BlockingQueue<Runnable> createBlockingQueue(int queues) {
        WorkQueueType queueType = WorkQueueType.byStr(System.getProperty(RpcSystemConfig.SystemPropertyThreadPoolQueueNameAttr, "LinkedBlockingQueue"));
        LOG.warn(queueType.getValue());
        switch (queueType) {
            case LINKED_BLOCKING_QUEUE:
                return new LinkedBlockingQueue<>();
            case ARRAY_BLOCKING_QUEUE:
                return new ArrayBlockingQueue<>(RpcSystemConfig.PARALLEL * queues);
            case SYNCHRONOUS_QUEUE:
                return new SynchronousQueue<>();
        }
        return null;
    }

    /*获取自定义线程池的拒绝任务策略*/
    private static RejectedExecutionHandler getRejectedPolicy() {
        RejectedPolicyType rejectedPolicyType = RejectedPolicyType.byStr(System.getProperty(RpcSystemConfig.SystemPropertyThreadPoolRejectedPolicyAttr, "AbortPolicyImp"));
        LOG.warn(rejectedPolicyType.getValue());
        switch (rejectedPolicyType) {
            case WAIT4_POLICY:
                return new Wait4PolicyImp();
            case CALLER_RUNS_POLICY:
                return new CallerRunsPolicyImp();
            case ABORT_POLICY:
                return new AbortPolicyImp();
            case BUY_TICKET_POLICY:
                return new BuyTicketPolicyImp();
            case DISCARDED_POLICY:
                return new DiscardedPolicyImp();
        }
        return null;
    }

    /**
     * 独立出线程池主要是为了应对复杂耗I/O操作的业务,不阻塞netty的handler线程而引入,当然如果业务足够简单，把处理逻辑写入netty的handler（ChannelInboundHandlerAdapter）也未尝不可
     */
    public static Executor getCustomizedExecutor(int poolSize, int queues) {
        String name = "RPC-ThreadPool";
        ThreadPoolExecutor executor = new ThreadPoolExecutor(poolSize, poolSize, 0, TimeUnit.MILLISECONDS, createBlockingQueue(queues),
                new CustomizedThreadFactory(name, true), getRejectedPolicy());
        return executor;
    }

    public static Executor getCustomizedExecutorWithJMX(int poolSize, int queues) {
        final ThreadPoolExecutor executor = (ThreadPoolExecutor) getCustomizedExecutor(poolSize, queues);
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                ThreadPoolStatus status = new ThreadPoolStatus();
                status.setPoolSize(executor.getPoolSize());
                status.setActiveCount(executor.getActiveCount());
                status.setCorePoolSize(executor.getCorePoolSize());
                status.setMaximumPoolSize(executor.getMaximumPoolSize());
                status.setLargestPoolSize(executor.getLargestPoolSize());
                status.setTaskCount(executor.getTaskCount());
                status.setCompletedTaskCount(executor.getCompletedTaskCount());
                try {
                    ThreadPoolMonitorProvider.monitor(status);
                } catch (IOException e) {
                    LOG.error("", e);
                } catch (MalformedObjectNameException e) {
                    LOG.error("", e);
                } catch (ReflectionException e) {
                    LOG.error("", e);
                } catch (MBeanException e) {
                    LOG.error("", e);
                } catch (InstanceNotFoundException e) {
                    LOG.error("", e);
                }
            }
        }, monitorDelay, monitorPeriod);
        return executor;
    }
}


package cn.gitv.bi.external.netty4rpc.thpool.customized;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/*线程工厂定义实现*/
public class CustomizedThreadFactory implements ThreadFactory {
    private static final AtomicInteger threadNumber = new AtomicInteger(1);

    private final AtomicInteger newThreadNum = new AtomicInteger(1);

    private final String prefix;

    private final boolean isDaemonThread;

    private final ThreadGroup threadGroup;

    public CustomizedThreadFactory() {
        this("rpcServer-default-threadPool-" + threadNumber.getAndIncrement(), false);
    }

    public CustomizedThreadFactory(String prefix) {
        this(prefix, false);
    }

    public CustomizedThreadFactory(String prefix, boolean isDaemon) {
        this.prefix = StringUtils.isNotEmpty(prefix) ? (prefix + "-thread-") : "";
        this.isDaemonThread = isDaemon;
        SecurityManager securityManager = System.getSecurityManager();
        threadGroup = (securityManager == null) ? Thread.currentThread().getThreadGroup() : securityManager.getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable runnable) {
        String name = prefix + newThreadNum.getAndIncrement();
        Thread instance = new Thread(threadGroup, runnable, name, 0);
        instance.setDaemon(isDaemonThread);
        return instance;
    }

    public ThreadGroup getThreadGroup() {
        return threadGroup;
    }
}


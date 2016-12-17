package cn.gitv.bi.external.netty4rpc.thpool.jmxstatus;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource
public class ThreadPoolStatus {
    private int poolSize;
    private int activeCount;
    private int corePoolSize;
    private int maximumPoolSize;
    private int largestPoolSize;
    private long taskCount;
    private long completedTaskCount;

    @ManagedOperation
    public int getPoolSize() {
        return poolSize;
    }

    @ManagedOperation
    public int getActiveCount() {
        return activeCount;
    }

    @ManagedOperation
    public int getCorePoolSize() {
        return corePoolSize;
    }

    @ManagedOperation
    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    @ManagedOperation
    public int getLargestPoolSize() {
        return largestPoolSize;
    }

    @ManagedOperation
    public long getTaskCount() {
        return taskCount;
    }

    @ManagedOperation
    public long getCompletedTaskCount() {
        return completedTaskCount;
    }

    @ManagedOperation
    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    @ManagedOperation
    public void setActiveCount(int activeCount) {
        this.activeCount = activeCount;
    }

    @ManagedOperation
    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    @ManagedOperation
    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    @ManagedOperation
    public void setLargestPoolSize(int largestPoolSize) {
        this.largestPoolSize = largestPoolSize;
    }

    @ManagedOperation
    public void setTaskCount(long taskCount) {
        this.taskCount = taskCount;
    }

    @ManagedOperation
    public void setCompletedTaskCount(long completedTaskCount) {
        this.completedTaskCount = completedTaskCount;
    }
}


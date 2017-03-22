package cn.gitv.bi.external.netty4rpc.core;

public class RpcSystemConfig {
    public static final String SystemPropertyThreadPoolRejectedPolicyAttr = "cn.gitv.bi.external.netty4rpc.thpool.policyimp";
    public static final String SystemPropertyThreadPoolQueueNameAttr = "cn.gitv.bi.external.netty4rpc.threadpool.queue";

    //方法返回到Java虚拟机的可用的处理器数量
    public static final int PARALLEL = Math.max(2, Runtime.getRuntime().availableProcessors());

    private static boolean monitorServerSupport = false;

    public static boolean isMonitorServerSupport() {
        return monitorServerSupport;
    }

    public static void setMonitorServerSupport(boolean jmxSupport) {
        monitorServerSupport = jmxSupport;
    }
}


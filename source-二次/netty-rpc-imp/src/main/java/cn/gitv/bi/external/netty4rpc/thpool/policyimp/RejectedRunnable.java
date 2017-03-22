package cn.gitv.bi.external.netty4rpc.thpool.policyimp;

public interface RejectedRunnable extends Runnable {
    void rejected();
}

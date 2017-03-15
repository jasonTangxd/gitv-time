package cn.gitv.bi.external.netty4rpc.spring;

import cn.gitv.bi.external.netty4rpc.core.RpcSystemConfig;
import cn.gitv.bi.external.netty4rpc.netty.serverimp.MessageRecExecutor;
import cn.gitv.bi.external.netty4rpc.serialize.RpcSerializeProtocol;
import cn.gitv.bi.external.netty4rpc.thpool.jmxstatus.ThreadPoolMonitorProvider;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class NettyRpcRegister implements InitializingBean, DisposableBean {
    private String ipAddr;
    private String protocol;
    private AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();


    @Override
    public void afterPropertiesSet() throws Exception {
        MessageRecExecutor messageRecvExecutor = MessageRecExecutor.getInstance();
        messageRecvExecutor.withServerAddress(ipAddr).withSerializeProtocol(Enum.valueOf(RpcSerializeProtocol.class, protocol));
        if (RpcSystemConfig.isMonitorServerSupport()) {
            context.register(ThreadPoolMonitorProvider.class);
            context.refresh();
        }

        messageRecvExecutor.start();
    }

    @Override
    public void destroy() throws Exception {
        MessageRecExecutor.getInstance().stop();
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}



package cn.gitv.bi.external.netty4rpc.netty.clientimp;

import cn.gitv.bi.external.netty4rpc.serialize.RpcSerializeProtocol;
import com.google.common.reflect.Reflection;

public class MessageSendExecutor {
    private static class MessageSendExecutorHolder {
        private static final MessageSendExecutor instance = new MessageSendExecutor();
    }

    public static MessageSendExecutor getInstance() {
        return MessageSendExecutorHolder.instance;
    }

    private RpcServerLoader rpcServerLoader = RpcServerLoader.getInstance();

    public MessageSendExecutor() {

    }

    public MessageSendExecutor(String serverAddress) {
        rpcServerLoader.load(serverAddress, RpcSerializeProtocol.JDKSERIALIZE);
    }

    public MessageSendExecutor(String serverAddress, RpcSerializeProtocol serializeProtocol) {
        rpcServerLoader.load(serverAddress, serializeProtocol);
    }

    public void setRpcServerLoader(String serverAddress, RpcSerializeProtocol serializeProtocol) {
        rpcServerLoader.load(serverAddress, serializeProtocol);
    }

    public void stop() {
        rpcServerLoader.unLoad();
    }

    public static <T> T execute(Class<T> rpcInterface) {
        return (T) Reflection.newProxy(rpcInterface, new MessageSendProxy<T>());
    }
}


package cn.gitv.bi.external.netty4rpc.netty.serverimp;

import cn.gitv.bi.external.netty4rpc.netty.serverimp.handler.*;
import cn.gitv.bi.external.netty4rpc.serialize.RpcSerializeFrame;
import cn.gitv.bi.external.netty4rpc.serialize.RpcSerializeProtocol;
import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import io.netty.channel.ChannelPipeline;

import java.util.Map;

public class RpcRecSerializeFrame implements RpcSerializeFrame {

    private Map<String, Object> serializeHandlerMap = null;

    public RpcRecSerializeFrame(Map<String, Object> handlerMap) {
        this.serializeHandlerMap = handlerMap;
    }

    private static ClassToInstanceMap<NettyRpcRecHandler> serializeHandler = MutableClassToInstanceMap.create();

    static {
        serializeHandler.putInstance(JdkNativeRecHandler.class, new JdkNativeRecHandler());
        serializeHandler.putInstance(KryoRecHandler.class, new KryoRecHandler());
        serializeHandler.putInstance(HessianRecHandler.class, new HessianRecHandler());
        serializeHandler.putInstance(ProtostuffRecHandler.class, new ProtostuffRecHandler());
    }

    public void select(RpcSerializeProtocol protocol, ChannelPipeline pipeline) {
        switch (protocol) {
            case JDKSERIALIZE: {
                serializeHandler.getInstance(JdkNativeRecHandler.class).handle(serializeHandlerMap, pipeline);
                break;
            }
            case KRYOSERIALIZE: {
                serializeHandler.getInstance(KryoRecHandler.class).handle(serializeHandlerMap, pipeline);
                break;
            }
            case HESSIANSERIALIZE: {
                serializeHandler.getInstance(HessianRecHandler.class).handle(serializeHandlerMap, pipeline);
                break;
            }
            case PROTOSTUFFSERIALIZE: {
                serializeHandler.getInstance(ProtostuffRecHandler.class).handle(serializeHandlerMap, pipeline);
                break;
            }
        }
    }
}

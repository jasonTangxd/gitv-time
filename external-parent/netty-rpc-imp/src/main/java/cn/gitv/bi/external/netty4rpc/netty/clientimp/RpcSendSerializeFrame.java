package cn.gitv.bi.external.netty4rpc.netty.clientimp;

import cn.gitv.bi.external.netty4rpc.netty.clientimp.handler.*;
import cn.gitv.bi.external.netty4rpc.serialize.RpcSerializeFrame;
import cn.gitv.bi.external.netty4rpc.serialize.RpcSerializeProtocol;
import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import io.netty.channel.ChannelPipeline;

public class RpcSendSerializeFrame implements RpcSerializeFrame {
    private static ClassToInstanceMap<NettyRpcSendHandler> handlerFactory = MutableClassToInstanceMap.create();

    static {
        handlerFactory.putInstance(JdkNativeSendHandler.class, new JdkNativeSendHandler());
        handlerFactory.putInstance(KryoSendHandler.class, new KryoSendHandler());
        handlerFactory.putInstance(HessianSendHandler.class, new HessianSendHandler());
        handlerFactory.putInstance(ProtostuffSendHandler.class, new ProtostuffSendHandler());
    }

    @Override
    public void select(RpcSerializeProtocol protocol, ChannelPipeline pipeline) {
        switch (protocol) {
            case JDKSERIALIZE: {
                handlerFactory.getInstance(JdkNativeSendHandler.class).handle(pipeline);
                break;
            }
            case KRYOSERIALIZE: {
                handlerFactory.getInstance(KryoSendHandler.class).handle(pipeline);
                break;
            }
            case HESSIANSERIALIZE: {
                handlerFactory.getInstance(HessianSendHandler.class).handle(pipeline);
                break;
            }
            case PROTOSTUFFSERIALIZE: {
                handlerFactory.getInstance(ProtostuffSendHandler.class).handle(pipeline);
                break;
            }
        }
    }
}


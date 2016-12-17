package cn.gitv.bi.external.netty4rpc.netty.serverimp;

import cn.gitv.bi.external.netty4rpc.serialize.RpcSerializeProtocol;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

import java.util.Map;

public class MessageRecChannelInitializer extends ChannelInitializer<SocketChannel> {

    private RpcSerializeProtocol protocol;
    private RpcRecSerializeFrame rpcRecvSerializeFrame = null;

    MessageRecChannelInitializer(Map<String, Object> handlerMap) {
        rpcRecvSerializeFrame = new RpcRecSerializeFrame(handlerMap);
    }

    MessageRecChannelInitializer buildRpcSerializeProtocol(RpcSerializeProtocol protocol) {
        if (protocol == null) throw new IllegalArgumentException("this RpcSerializeProtocol must not be null!");
        this.protocol = protocol;
        return this;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        rpcRecvSerializeFrame.select(protocol, pipeline);
    }
}

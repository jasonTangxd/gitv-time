package cn.gitv.bi.external.netty4rpc.serialize;

import io.netty.channel.ChannelPipeline;

public interface RpcSerializeFrame {
    void select(RpcSerializeProtocol protocol, ChannelPipeline pipeline);
}


package cn.gitv.bi.external.netty4rpc.netty.clientimp.handler;

import io.netty.channel.ChannelPipeline;

public interface NettyRpcSendHandler {
    void handle(ChannelPipeline pipeline);
}


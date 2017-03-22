package cn.gitv.bi.external.netty4rpc.netty.serverimp.handler;

import io.netty.channel.ChannelPipeline;

import java.util.Map;

public interface NettyRpcRecHandler {
    void handle(Map<String, Object> handlerMap, ChannelPipeline pipeline);
}


package cn.gitv.bi.external.netty4rpc.netty.serverimp;

import cn.gitv.bi.external.netty4rpc.model.MessageRequest;
import cn.gitv.bi.external.netty4rpc.model.MessageResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Map;

public class MessageRecHandler extends ChannelInboundHandlerAdapter {

    private final Map<String, Object> handlerMap;

    public MessageRecHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageRequest request = (MessageRequest) msg;
        MessageResponse response = new MessageResponse();
        MessageRecInitializeTask recvTask = new MessageRecInitializeTask(request, response, handlerMap);
        MessageRecExecutor.submit(recvTask, ctx, request, response);
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}


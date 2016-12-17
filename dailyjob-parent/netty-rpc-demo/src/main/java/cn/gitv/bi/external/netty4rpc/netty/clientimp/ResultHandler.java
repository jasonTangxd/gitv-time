package cn.gitv.bi.external.netty4rpc.netty.clientimp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Kang on 2016/12/12.
 */
public class ResultHandler extends ChannelInboundHandlerAdapter {
    private static Logger LOG = LoggerFactory.getLogger(ResultHandler.class);
    private Object response;

    public Object getResponse() {
        return response;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        response = msg;
        LOG.info("client接收到服务器返回的消息:" + msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.info("client exception is general");
    }
}

package cn.gitv.bi.external.netty4rpc.netty.serverimp;

import cn.gitv.bi.external.netty4rpc.model.MessageRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Kang on 2016/12/12.
 */
public class InvokerHandler extends ChannelInboundHandlerAdapter {
    public static ConcurrentHashMap<String, Object> classMap = new ConcurrentHashMap<String, Object>();
    private static Logger LOG = LoggerFactory.getLogger(ServertStart.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageRequest messageRequest = (MessageRequest) msg;
        Object claszz = null;
        if (!classMap.containsKey(messageRequest.getClassName())) {
            try {
                claszz = Class.forName(messageRequest.getClassName()).newInstance();
                classMap.put(messageRequest.getClassName(), claszz);
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                LOG.error("", e);
            }
        } else {
            claszz = classMap.get(messageRequest.getClassName());
        }
        Method method = claszz.getClass().getMethod(messageRequest.getMethodName(), messageRequest.getTypeParameters());
        Object result = method.invoke(claszz, messageRequest.getParameters());
        ctx.write(result);
        ctx.flush();
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

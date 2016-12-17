package cn.gitv.bi.external.netty4rpc.netty.clientimp;

import cn.gitv.bi.external.netty4rpc.core.MessageCallBack;
import cn.gitv.bi.external.netty4rpc.model.MessageRequest;
import com.google.common.reflect.AbstractInvocationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.UUID;

public class MessageSendProxy<T> extends AbstractInvocationHandler {
    private static Logger LOG = LoggerFactory.getLogger(MessageSendProxy.class);

    @Override
    public Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
        //对MessageRequest实例封装
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setMessageId(UUID.randomUUID().toString());
        messageRequest.setClassName(method.getDeclaringClass().getName());
        messageRequest.setMethodName(method.getName());
        messageRequest.setTypeParameters(method.getParameterTypes());
        messageRequest.setParameters(args);
        LOG.info("{}----{}",messageRequest.toString(),messageRequest.getParameters());
        MessageSendHandler messageSendHandler = RpcServerLoader.getInstance().getMessageSendHandler();
        MessageCallBack callBack = messageSendHandler.sendRequest(messageRequest);
        return callBack.start();
    }
}


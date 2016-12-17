package cn.gitv.bi.external.netty4rpc.netty.clientimp;

import cn.gitv.bi.external.netty4rpc.model.MessageRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by Kang on 2016/12/12.
 */
public class InvocationHandlerImp implements InvocationHandler {
    private Object target;

    public InvocationHandlerImp(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {

        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setClassName(target.getClass().getName());
        messageRequest.setMethodName(method.getName());
        messageRequest.setParameters(args);
        messageRequest.setTypeParameters(method.getParameterTypes());

        ResultHandler resultHandler = new ResultHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                            pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
                            pipeline.addLast("encoder", new ObjectEncoder());
                            pipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                            pipeline.addLast("handler", resultHandler);
                        }
                    });

            ChannelFuture future = b.connect("localhost", 8081).sync();
            future.channel().writeAndFlush(messageRequest).sync();
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
        return resultHandler.getResponse();
    }
}

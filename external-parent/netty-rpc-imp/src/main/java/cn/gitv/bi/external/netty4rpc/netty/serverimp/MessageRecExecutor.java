package cn.gitv.bi.external.netty4rpc.netty.serverimp;

import cn.gitv.bi.external.netty4rpc.core.RpcSystemConfig;
import cn.gitv.bi.external.netty4rpc.model.MessageKeyVal;
import cn.gitv.bi.external.netty4rpc.model.MessageRequest;
import cn.gitv.bi.external.netty4rpc.model.MessageResponse;
import cn.gitv.bi.external.netty4rpc.serialize.RpcSerializeProtocol;
import cn.gitv.bi.external.netty4rpc.thpool.customized.CustomizedThreadFactory;
import cn.gitv.bi.external.netty4rpc.thpool.customized.RpcCustomizedThreadPool;
import com.google.common.util.concurrent.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

public class MessageRecExecutor implements ApplicationContextAware {
    private static Logger LOG = LoggerFactory.getLogger(MessageRecExecutor.class);
    private String serverAddress;
    private RpcSerializeProtocol serializeProtocol = RpcSerializeProtocol.JDKSERIALIZE;
    private final static String DELIMITER = ":";
    int parallel = RpcSystemConfig.PARALLEL * 2;
    private static int threadNums = 16;
    private static int queueNums = -1;

    private Map<String, Object> handlerMap = new ConcurrentHashMap<>();

    volatile private static ListeningExecutorService listeningExecutorService;
    ThreadFactory threadRpcFactory = new CustomizedThreadFactory("NettyRPC ThreadFactory");

    EventLoopGroup boss = new NioEventLoopGroup();
    EventLoopGroup worker = new NioEventLoopGroup(parallel, threadRpcFactory, SelectorProvider.provider());

    public Map<String, Object> getHandlerMap() {
        return handlerMap;
    }

    public void setHandlerMap(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public MessageRecExecutor withServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
        return this;
    }

    public MessageRecExecutor withSerializeProtocol(RpcSerializeProtocol serializeProtocol) {
        this.serializeProtocol = serializeProtocol;
        return this;
    }
    public RpcSerializeProtocol getSerializeProtocol() {
        return serializeProtocol;
    }


    private static class MessageRecvExecutorHolder {
        static final MessageRecExecutor instance = new MessageRecExecutor();
    }

    public static MessageRecExecutor getInstance() {
        return MessageRecvExecutorHolder.instance;
    }

    public static void submit(Callable<Boolean> task, final ChannelHandlerContext ctx, final MessageRequest request, final MessageResponse response) {
        if (listeningExecutorService == null) {
            synchronized (MessageRecExecutor.class) {
                if (listeningExecutorService == null) {
                    listeningExecutorService = MoreExecutors.listeningDecorator((ThreadPoolExecutor) (RpcSystemConfig.isMonitorServerSupport() ? RpcCustomizedThreadPool.getCustomizedExecutorWithJMX(threadNums, queueNums) : RpcCustomizedThreadPool.getCustomizedExecutor(threadNums, queueNums)));
                }
            }
        }

        ListenableFuture<Boolean> listenableFuture = listeningExecutorService.submit(task);
        Futures.addCallback(listenableFuture, new FutureCallback<Boolean>() {
            public void onSuccess(Boolean result) {
                ctx.writeAndFlush(response).addListener(new ChannelFutureListener() {
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        LOG.info("RPC Server Send message-id respone{}:", request.getMessageId());
                    }
                });
            }

            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        try {
            MessageKeyVal keyVal = (MessageKeyVal) ctx.getBean(Class.forName("cn.gitv.bi.external.netty4rpc.model.MessageKeyVal"));
            Map<String, Object> rpcServiceObject = keyVal.getMessageKeyVal();

            Set s = rpcServiceObject.entrySet();
            Iterator<Map.Entry<String, Object>> it = s.iterator();
            Map.Entry<String, Object> entry;

            while (it.hasNext()) {
                entry = it.next();
                handlerMap.put(entry.getKey(), entry.getValue());
            }
        } catch (ClassNotFoundException ex) {
            LOG.error("", ex);
        }
    }

    public void start() {
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker).channel(NioServerSocketChannel.class)
                    .childHandler(new MessageRecChannelInitializer(handlerMap).buildRpcSerializeProtocol(serializeProtocol))
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            String[] ipAddress = serverAddress.split(MessageRecExecutor.DELIMITER);
            if (ipAddress.length == 2) {
                String host = ipAddress[0];
                int port = Integer.parseInt(ipAddress[1]);
                ChannelFuture future = bootstrap.bind(host, port).sync();
                LOG.info("Netty RPC Server start success!\nip:{}\nport:{}\nprotocol:{}\n\n", host, port, serializeProtocol);
                future.channel().closeFuture().sync();
            } else {
                LOG.info("Netty RPC Server start fail!\n");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        worker.shutdownGracefully();
        boss.shutdownGracefully();
    }
}

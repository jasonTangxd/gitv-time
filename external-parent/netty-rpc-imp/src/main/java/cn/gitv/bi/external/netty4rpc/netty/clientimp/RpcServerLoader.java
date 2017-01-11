package cn.gitv.bi.external.netty4rpc.netty.clientimp;

import cn.gitv.bi.external.netty4rpc.core.RpcSystemConfig;
import cn.gitv.bi.external.netty4rpc.serialize.RpcSerializeProtocol;
import cn.gitv.bi.external.netty4rpc.thpool.customized.RpcCustomizedThreadPool;
import com.google.common.util.concurrent.*;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RpcServerLoader {
    private static Logger LOG = LoggerFactory.getLogger(RpcServerLoader.class);
    volatile private static RpcServerLoader rpcServerLoader;
    private final static String DELIMITER = ":";
    private RpcSerializeProtocol serializeProtocol = RpcSerializeProtocol.JDKSERIALIZE;
    private final static int parallel = RpcSystemConfig.PARALLEL * 2;
    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup(parallel);
    private static ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator((ThreadPoolExecutor) RpcCustomizedThreadPool.getCustomizedExecutor(16, -1));
    private MessageSendHandler messageSendHandler = null;
    private Lock lock = new ReentrantLock();
    private Condition connectStatus = lock.newCondition();
    private Condition handlerStatus = lock.newCondition();

    private RpcServerLoader() {
    }

    //单例
    public static RpcServerLoader getInstance() {
        if (rpcServerLoader == null) {
            synchronized (RpcServerLoader.class) {
                if (rpcServerLoader == null) {
                    rpcServerLoader = new RpcServerLoader();
                }
            }
        }
        return rpcServerLoader;
    }

    public void load(String serverAddress, RpcSerializeProtocol serializeProtocol) {
        String[] ipAddress = serverAddress.split(RpcServerLoader.DELIMITER);
        if (ipAddress.length == 2) {
            String host = ipAddress[0];
            int port = Integer.parseInt(ipAddress[1]);
            final InetSocketAddress remoteAddress = new InetSocketAddress(host, port);
            ListenableFuture<Boolean> listenableFuture = listeningExecutorService.submit(new MessageSendInitializeTask(eventLoopGroup, remoteAddress, serializeProtocol));
            LOG.info("Netty RPC Client cn.bi.gitv.hip.parquetdemo.start success!\nip:{}\nport:{}\nprotocol:{}\n\n", host, port, serializeProtocol);

            Futures.addCallback(listenableFuture, new FutureCallback<Boolean>() {
                public void onSuccess(Boolean result) {
                    try {
                        lock.lock();
                        if (messageSendHandler == null) {
                            handlerStatus.await();
                        }

                        if (result == Boolean.TRUE && messageSendHandler != null) {
                            connectStatus.signalAll();
                        }
                    } catch (InterruptedException ex) {
                        LOG.error("", ex);
                    } finally {
                        lock.unlock();
                    }
                }

                public void onFailure(Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    public void setMessageSendHandler(MessageSendHandler messageInHandler) {
        try {
            lock.lock();
            this.messageSendHandler = messageInHandler;
            handlerStatus.signal();
        } finally {
            lock.unlock();
        }
    }

    public MessageSendHandler getMessageSendHandler() throws InterruptedException {
        try {
            lock.lock();
            if (messageSendHandler == null) {
                connectStatus.await();
            }
            return messageSendHandler;
        } finally {
            lock.unlock();
        }
    }

    public void unLoad() {
        messageSendHandler.close();
        listeningExecutorService.shutdown();
        eventLoopGroup.shutdownGracefully();
    }

    public void setSerializeProtocol(RpcSerializeProtocol serializeProtocol) {
        this.serializeProtocol = serializeProtocol;
    }
}

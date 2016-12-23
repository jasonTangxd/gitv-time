package cn.gitv.bi.userinfo.rmconsumer.thread;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;

import static cn.gitv.bi.userinfo.rmconsumer.constant.Constant.LIMIT_NUM;

public class NumConditionTask extends TimerTask {
    private Logger LOG = LoggerFactory.getLogger(NumConditionTask.class);
    private Channel channel = null;
    private String queueName = null;
    BlockingQueue<String> blockingQueue;

    public NumConditionTask(String routingKey, Channel channel, BlockingQueue<String> blockingQueue) {
        this.queueName = routingKey;
        this.channel = channel;
        this.blockingQueue = blockingQueue;
    }

    public void run() {
        //每秒调度一次数量
        try {
            long num = channel.messageCount(queueName);
            if (num > LIMIT_NUM) {
                for (int i = 0; i < num; i++) {
                    GetResponse gp = channel.basicGet(queueName, true);
                    if (gp == null) {
                        return;
                    }
                    String content = new String(gp.getBody());
                    boolean isNotFull = blockingQueue.offer(content);
                    if (isNotFull) {
                        //正常存入queue打印log
                        LOG.info("{} of NumConditionTask put {} to queue", queueName, content);
                    } else {
                        //队列满了,不再进行本次存取,并阻塞式把当前这条记录存入
                        LOG.info("{} of NumConditionTask queue is full", queueName, content);
                        blockingQueue.put(content);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("", e);
        }

    }
}

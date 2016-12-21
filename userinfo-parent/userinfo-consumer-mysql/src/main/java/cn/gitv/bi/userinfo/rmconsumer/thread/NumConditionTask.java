package cn.gitv.bi.userinfo.rmconsumer.thread;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;

import static cn.gitv.bi.userinfo.rmconsumer.constant.Constant.LIMIT_NUM;

public class NumConditionTask extends TimerTask {
    private Logger logger = LoggerFactory.getLogger(NumConditionTask.class);
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
                    blockingQueue.put(content);
                }
            } else {
                logger.debug("[{}] num of NumConditionTask is less {},so pass this time!", queueName, LIMIT_NUM);
            }
        } catch (Exception e) {
            logger.error("", e);
        }

    }
}

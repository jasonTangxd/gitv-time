package cn.gitv.bi.userinfo.rmconsumer.thread;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;


public class TimeConditionTask extends TimerTask {
    private Logger logger = LoggerFactory.getLogger(TimeConditionTask.class);
    private Channel channel = null;
    private String queueName = null;
    private BlockingQueue<String> blockingQueue = null;

    public TimeConditionTask(String routingKey, Channel channel, BlockingQueue<String> blockingQueue) {
        this.channel = channel;
        this.queueName = routingKey;
        this.blockingQueue = blockingQueue;
    }

    public void run() {
        //每十分钟调度一次该函数
        try {
            long num = channel.messageCount(queueName);
            if (num == 0) {
                return;
            } else {
                for (int i = 0; i < num; i++) {
                    GetResponse gp = channel.basicGet(queueName, true);
                    if (gp == null) {
                        return;
                    }
                    String content = new String(gp.getBody());
                    blockingQueue.put(content);
                }
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }


}

package cn.gitv.bi.userinfo.rmconsumer.thread;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;


public class TimeConditionTask extends TimerTask {
    private static Logger LOG = LoggerFactory.getLogger(TimeConditionTask.class);
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
                    //把从rabbit中获取的mac存入queue中
                    boolean isNotFull = blockingQueue.offer(content);
                    if (isNotFull) {
                        //正常存入queue打印log
                        LOG.info("{} of TimeConditionTask put {} to queue", queueName, content);
                    } else {
                        //队列满了,不再进行本次存取,并阻塞式把当前这条记录存入
                        LOG.info("{} of TimeConditionTask queue is full", queueName, content);
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

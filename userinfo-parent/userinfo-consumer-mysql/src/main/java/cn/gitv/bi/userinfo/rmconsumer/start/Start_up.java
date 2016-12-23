package cn.gitv.bi.userinfo.rmconsumer.start;

import cn.gitv.bi.userinfo.rmconsumer.thread.NumConditionTask;
import cn.gitv.bi.userinfo.rmconsumer.thread.QueueConsumer;
import cn.gitv.bi.userinfo.rmconsumer.thread.TimeConditionTask;
import cn.gitv.bi.userinfo.rmconsumer.utils.CassandraConnection;
import cn.gitv.bi.userinfo.rmconsumer.utils.RabbitConnection;
import cn.gitv.bi.userinfo.rmconsumer.utils.ZkObserverUtils;
import com.datastax.driver.core.Session;
import com.rabbitmq.client.Channel;

import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import static cn.gitv.bi.userinfo.rmconsumer.constant.Constant.DELAY_TIME;
import static cn.gitv.bi.userinfo.rmconsumer.constant.Constant.PERIOD_TIME;

/**
 *
 */
public class Start_up {
    // private static Logger log = LoggerFactory.getLogger(Start_up.class);
    private static Session session = CassandraConnection.getSession();
    private static Channel channel = RabbitConnection.getChannel();
    private static ExecutorService executorService = Executors.newFixedThreadPool(5);

    public static void main(String[] args) throws Exception {
        //根据可操作partnerList启动定时器
        for (String routingKey : ZkObserverUtils.getCanUsePartnerList()) {
            //为每个可同步的partner启动一个线程安全的queue
            BlockingQueue syncQueue = new LinkedBlockingQueue(5000);
            //启动时间维度的定时器
            new Timer().scheduleAtFixedRate(new TimeConditionTask(routingKey, channel, syncQueue), DELAY_TIME, PERIOD_TIME);
            //启动数量维度的定时器
            new Timer().scheduleAtFixedRate(new NumConditionTask(routingKey, channel, syncQueue), 0, 1000);
            executorService.submit(new QueueConsumer(routingKey, session, syncQueue));
        }

    }


}

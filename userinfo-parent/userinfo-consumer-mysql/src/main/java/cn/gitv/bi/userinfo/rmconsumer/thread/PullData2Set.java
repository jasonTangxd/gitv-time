package cn.gitv.bi.userinfo.rmconsumer.thread;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Kang on 2016/12/7.
 */
public class PullData2Set {
    public static ExecutorService pool = Executors.newFixedThreadPool(30);
    private static Logger logger = LoggerFactory.getLogger(PullData2Set.class);

    public static Set<String> pullfromMysql(Channel channel, String queueName, long num) {
        Set<String> macList = new HashSet<String>();
        // 将num数量的mac放入set集合中
        for (int i = 0; i < num; i++) {
            try {
                // 从rabbitMQ中获取num数量的消息
                GetResponse gp = channel.basicGet(queueName, true);
                if (gp == null) {
                    return null;
                }
                String content = new String(gp.getBody());
                macList.add(content);
            } catch (IOException e) {
                logger.error("", e);
            }
        }
        return macList;
    }
}

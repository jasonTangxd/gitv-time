package cn.gitv.bi.userinfo.rmconsumer.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import static cn.gitv.bi.userinfo.rmconsumer.constant.Properties.*;

public class RabbitConnection {
    private static Connection connection = null;
    private static Channel channel = null;
    private static ConnectionFactory factory = null;


    static {
        factory = new ConnectionFactory();
        factory.setHost(RAB_HOST);
        factory.setUsername(RAB_USERNAME);
        factory.setPassword(RAB_PASSWORD);
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare(RAB_EXCHANGE_NAME, "direct");
        } catch (Exception e) {
            //如果无法正常连接作为rm的消费者，程序终止，重新配置提交
            throw new IllegalArgumentException("can not connect rebbit in right way!");
        }
    }

    /**
     * todo 这个channel制定了路由转换规则direct
     *
     * @return 返回channel, channel是线程安全的
     */
    public static synchronized Channel getChannel() {
        if (channel == null) {
            try {
                channel = connection.createChannel();
                channel.exchangeDeclare(RAB_EXCHANGE_NAME, "direct");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return channel;
    }

}

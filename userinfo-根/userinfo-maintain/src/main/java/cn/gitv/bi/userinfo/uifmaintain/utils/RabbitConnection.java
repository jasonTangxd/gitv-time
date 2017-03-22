package cn.gitv.bi.userinfo.uifmaintain.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

public class RabbitConnection {
    private static Connection connection = null;
    private static Channel channel = null;
    private static ConnectionFactory factory = null;
    private static final String HOST = "10.10.121.103";
    private static final String USERNAME = "likang";
    private static final String PASSWORD = "122726894";
    public static final String EXCHANGE_NAME = "myExchange";

    static {
        factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        } catch (Exception e) {
            e.printStackTrace();
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
                channel.exchangeDeclare(EXCHANGE_NAME, "direct");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return channel;
    }

}

package cn.gitv.bi.userinfo.rmconsumer.thread;

import cn.gitv.bi.userinfo.rmconsumer.syndata.Super_SynData;
import cn.gitv.bi.userinfo.rmconsumer.utils.JDBCPoolUtils;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;
import java.util.TimerTask;

import static cn.gitv.bi.userinfo.rmconsumer.constant.Constant.LIMIT_NUM;
import static cn.gitv.bi.userinfo.rmconsumer.thread.PullData2Set.pool;

public class NumConditionTask extends TimerTask {
    private Logger logger = LoggerFactory.getLogger(NumConditionTask.class);
    private Channel channel = null;
    private String queueName = null;
    private Super_SynData synInstance = null;
    private Session session;
    private PreparedStatement ps;

    public NumConditionTask(Channel channel, String queueName, Super_SynData synInstance, Session session, PreparedStatement ps) {
        this.channel = channel;
        this.queueName = queueName;
        this.synInstance = synInstance;
        this.session = session;
        this.ps = ps;
    }

    public void run() {
        //每秒调度一次数量
        try {
            long num = channel.messageCount(queueName);
            if (num > LIMIT_NUM) {
                Set<String> macList = PullData2Set.pullfromMysql(channel, queueName, LIMIT_NUM);
                logger.debug("[{}] NumConditionTask is called,and content is {}", queueName, macList);
                pool.execute(new TransRuner(macList, synInstance, JDBCPoolUtils.getConnection(queueName), session, ps));
            } else {
                logger.debug("[{}] num of NumConditionTask is less {},so pass this time!", queueName, LIMIT_NUM);
            }
        } catch (IOException e) {
            logger.error("", e);
        }

    }
}

package cn.gitv.bi.userinfo.rmconsumer.thread;

import cn.gitv.bi.userinfo.rmconsumer.syndata.Super_SynData;
import cn.gitv.bi.userinfo.rmconsumer.syndata.SynFactory;
import cn.gitv.bi.userinfo.rmconsumer.utils.CassandraConnection;
import cn.gitv.bi.userinfo.rmconsumer.utils.JDBCPoolUtils;
import cn.gitv.bi.userinfo.rmconsumer.utils.RabbitConnection;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import static cn.gitv.bi.userinfo.rmconsumer.thread.PullData2Set.pool;

public class TimeConditionTask extends TimerTask {
    private Logger logger = LoggerFactory.getLogger(TimeConditionTask.class);
    private static final String UPDATE_UIF = "update user_info set main_account_id=?,child_account_id=?,user_id=?,account_time=?,activate_time=?,status=?,user_type=?,province=?,city_name=?,area_name=? where partner=? and mac_addr=?;";
    private static Channel channel = RabbitConnection.getChannel();
    private String queueName = null;
    private Super_SynData synInstance = null;
    private static Session session = CassandraConnection.getSession();
    private static PreparedStatement ps = session.prepare(UPDATE_UIF);

    public TimeConditionTask(String routingKey) {
        synInstance = SynFactory.getSynInstance(routingKey).withInit(routingKey);
        try {
            queueName = routingKey;
            new Timer().scheduleAtFixedRate(new NumConditionTask(channel, queueName, synInstance, session, ps), 0, 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        //每十分钟调度一次该函数
        try {
            long num = channel.messageCount(queueName);
            if (num == 0) {
                return;
            } else {
                Set<String> macList = PullData2Set.pullfromMysql(channel, queueName, num);
                logger.debug("TimeConditionTask is called,and content {}", macList);
                pool.execute(new TransRuner(macList, synInstance, JDBCPoolUtils.getConnection(queueName), session, ps));
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }


}

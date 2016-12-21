package cn.gitv.bi.userinfo.rmconsumer.thread;

import cn.gitv.bi.userinfo.rmconsumer.bean.UserInfo;
import cn.gitv.bi.userinfo.rmconsumer.syndata.SuperSynData;
import cn.gitv.bi.userinfo.rmconsumer.syndata.SynFactory;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;

/**
 * Created by Kang on 2016/12/7.
 */
public class QueueConsumer implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(QueueConsumer.class);
    private static final String UPDATE_UIF = "update user_info set main_account_id=?,child_account_id=?,user_id=?,account_time=?,activate_time=?,status=?,user_type=?,province=?,city_name=?,area_name=? where partner=? and mac_addr=?;";
    private SuperSynData synInstance = null;
    private Session session = null;
    private PreparedStatement ps = null;
    private BlockingQueue<String> blockingQueue = null;

    public QueueConsumer(String routingKey, Session session, BlockingQueue<String> blockingQueue) {
        this.blockingQueue = blockingQueue;
        this.synInstance = SynFactory.getSynInstance(routingKey).withInit(routingKey);
        this.session = session;
        this.ps = this.session.prepare(UPDATE_UIF);
    }

    @Override
    public void run() {
        try {
            String mac = blockingQueue.take();
            UserInfo uif = synInstance.getFromMysql(mac);
            if (uif != null)
                synInstance.upToCassandra(uif, session, ps);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

}

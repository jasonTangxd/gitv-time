package cn.gitv.bi.userinfo.rmconsumer.thread;

import cn.gitv.bi.userinfo.rmconsumer.bean.UserInfo;
import cn.gitv.bi.userinfo.rmconsumer.syndata.SuperSynData;
import cn.gitv.bi.userinfo.rmconsumer.syndata.SynFactory;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Kang on 2016/12/7.
 */
public class QueueConsumer extends TimerTask {
    private static Logger logger = LoggerFactory.getLogger(QueueConsumer.class);
    private static final String UPDATE_UIF = "update user_info set main_account_id=?,child_account_id=?,user_id=?,account_time=?,activate_time=?,status=?,user_type=?,province=?,city_name=?,area_name=? where partner=? and mac_addr=?;";
    private SuperSynData synInstance = null;
    private Session session = null;
    private BlockingQueue<String> blockingQueue = null;
    private String routingKey;
    private PreparedStatement ps = null;

    public QueueConsumer(String routingKey, Session session, BlockingQueue<String> blockingQueue) {
        this.session = session;
        this.routingKey = routingKey;
        this.blockingQueue = blockingQueue;
        this.synInstance = SynFactory.getSynInstance(routingKey).withInit(routingKey);
        this.ps = session.prepare(UPDATE_UIF);
    }


    @Override
    public void run() {
        while (true) {
            try {
                //从queue中获取一条数据
                String mac = blockingQueue.poll();
                if (mac == null) {
                    // 如果队列中没有数据，线程sleep
//                    logger.debug("{} blockingQueue.poll() get null,so will sleep 1s", routingKey);
                    Thread.sleep(1000);
                    continue;
                }
                UserInfo uif = synInstance.getFromMysql(mac);
                logger.info("{} of consumer get one data [{}] from mysql", routingKey, uif);
                if (uif != null) {
                    //将从mysql中获取到的数据存入cassandra
                    synInstance.upToCassandra(uif, session, ps);
                    logger.info("{} put uif from mysql to cass", routingKey);
                } else {
                    //no found mysql data 插入cass
                    String ts = System.currentTimeMillis() + "";
                    String noFoundMac = "update not_found_from_mysql set ts='" + ts + "' where partner ='" + routingKey + "'" + "and mac='" + mac + "'";
                    session.execute(noFoundMac);
                    logger.info("{} put not found mac from mysql to cass", routingKey);
                }
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }

}

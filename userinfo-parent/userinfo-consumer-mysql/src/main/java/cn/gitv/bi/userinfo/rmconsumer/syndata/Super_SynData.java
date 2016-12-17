package cn.gitv.bi.userinfo.rmconsumer.syndata;

import cn.gitv.bi.userinfo.rmconsumer.bean.UserInfo;
import cn.gitv.bi.userinfo.rmconsumer.storage.Mapper;
import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.List;
import java.util.Set;

public abstract class Super_SynData {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    //status:1=开户 2=销户 3=换机 4=暂停 5=复机 6=激活 7=停机
    public abstract Super_SynData withInit(String partner);

    public abstract List<UserInfo> getFromMysql(Set<String> mac_list, Connection safeConnection);

    public void upToCassandra(List<UserInfo> userList, Session session, PreparedStatement ps) {
        if (userList == null | userList.size() == 0) {
            return;
        }
        BatchStatement batch = new BatchStatement();
        try {
            for (UserInfo item : userList) {
//                logger.debug("[{}] will update:{}", this.getClass(), item);
                BoundStatement bs = Mapper.update_uif(ps, item);
                batch.add(bs);
            }
            logger.debug("Thread: [{}] use [{}] execute batch and size is-->:{}", Thread.currentThread(), this.getClass(), batch.size());
            session.execute(batch);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

}

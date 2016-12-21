package cn.gitv.bi.userinfo.rmconsumer.syndata;

import cn.gitv.bi.userinfo.rmconsumer.bean.UserInfo;
import cn.gitv.bi.userinfo.rmconsumer.storage.Mapper;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SuperSynData {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    //status:1=开户 2=销户 3=换机 4=暂停 5=复机 6=激活 7=停机
    public abstract SuperSynData withInit(String partner);

    public abstract UserInfo getFromMysql(String mac);

    public abstract void initConnection();

    public void upToCassandra(final UserInfo item, Session session, PreparedStatement ps) {
        try {
            BoundStatement bs = Mapper.update_uif(ps, item);
            ListenableFuture<ResultSet> listenableFuture = session.executeAsync(bs);
            Futures.addCallback(listenableFuture, new FutureCallback<ResultSet>() {
                public void onSuccess(ResultSet result) {
                    logger.info("Thread[{}]`s partner [{}] execute up2Cass is sucess!", Thread.currentThread(), item.getPartner());
                }

                public void onFailure(Throwable t) {
                    logger.error("Thread[{}]`s partner [{}] execute up2Cass is failed!", Thread.currentThread(), item.getPartner());
                }
            });
        } catch (Exception e) {
            logger.error("", e);
        }
    }

}

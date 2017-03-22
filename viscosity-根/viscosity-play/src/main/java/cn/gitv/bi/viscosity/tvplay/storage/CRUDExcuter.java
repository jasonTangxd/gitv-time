package cn.gitv.bi.viscosity.tvplay.storage;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.google.common.util.concurrent.ListenableFuture;

public class CRUDExcuter {

    public static ListenableFuture<ResultSet> insert_statement(Session session, BoundStatement bs) {
        return session.executeAsync(bs);
    }

    public static ListenableFuture<ResultSet> async_read_statement(Session session, BoundStatement bs) {
        return session.executeAsync(bs);
    }

}

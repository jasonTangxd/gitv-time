package cn.gitv.bi.userinfo.uifmaintain.storage;

import cn.gitv.bi.userinfo.uifmaintain.constant.LogConst;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.google.common.base.Function;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncBack {
    private static ExecutorService executor = Executors.newFixedThreadPool(6);

    //
    public static void main_isnull(final Session session, BoundStatement read, final OutputCollector collector,
                                   final Tuple input) {
        ListenableFuture<ResultSet> lf = CRUDExcuter.async_read_statement(session, read);
        ListenableFuture<Long> isExists = Futures.transform(lf, new Function<ResultSet, Long>() {
            public Long apply(ResultSet rs) {
                return rs.one().getLong(0);
            }
        });
        Futures.addCallback(isExists, new FutureCallback<Long>() {
            public void onFailure(Throwable t) {
                collector.fail(input);
            }

            public void onSuccess(Long backCode) {
                if (backCode == 0) {
                    collector.emit(LogConst.RAB, input, new Values(input.getStringByField("mac"), input.getStringByField("partner")));
                }
                collector.ack(input);
            }
        }, executor);
    }

    //
    public static void fopen_isnull(final Session session, BoundStatement read, final OutputCollector collector,
                                    final Tuple input) {
        ListenableFuture<ResultSet> lf = CRUDExcuter.async_read_statement(session, read);
        ListenableFuture<Long> isExists = Futures.transform(lf, new Function<ResultSet, Long>() {
            public Long apply(ResultSet rs) {
                return rs.one().getLong(0);
            }
        });
        Futures.addCallback(isExists, new FutureCallback<Long>() {
            public void onFailure(Throwable t) {
                collector.fail(input);
            }

            public void onSuccess(Long backCode) {
                if (backCode == 0) {
                    collector.emit(input, new Values(input.getStringByField("mac"), input.getStringByField("partner"), input.getStringByField("last_open")));
                }
                collector.ack(input);
            }
        }, executor);
    }

    //
    public static void appversion_isnull(final Session session, BoundStatement read, final OutputCollector collector,
                                         final Tuple input) {
        ListenableFuture<ResultSet> lf = CRUDExcuter.async_read_statement(session, read);
        ListenableFuture<Long> isExists = Futures.transform(lf, new Function<ResultSet, Long>() {
            public Long apply(ResultSet rs) {
                return rs.one().getLong(0);
            }
        });
        Futures.addCallback(isExists, new FutureCallback<Long>() {
            public void onFailure(Throwable t) {
                collector.fail(input);
            }

            public void onSuccess(Long backCode) {
                if (backCode == 0) {
                    collector.emit(input, new Values(input.getStringByField("enumtype"), input.getStringByField("mac"), input.getStringByField("partner"), input.getStringByField("app_version")));
                }
                collector.ack(input);
            }
        }, executor);
    }

    //
    public static void update_back(final Session session, BoundStatement bs, final OutputCollector collector,
                                   final Tuple input) {
        ListenableFuture<ResultSet> lf = CRUDExcuter.insert_statement(session, bs);
        Futures.addCallback(lf, new FutureCallback<ResultSet>() {
            public void onSuccess(ResultSet result) {
                collector.ack(input);
            }

            public void onFailure(Throwable t) {
                collector.fail(input);
            }
        }, executor);
    }
}

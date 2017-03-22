package cn.gitv.bi.viscosity.tvplay.storage;
import cn.gitv.bi.viscosity.tvplay.constant.Constant;
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

public class AsyncBack_Liv {
    private static ExecutorService executor = Executors.newFixedThreadPool(6);

    public static void user_read_async(final Session session, BoundStatement read, final OutputCollector collector,
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
                    collector.emit(Constant.LIV_TO_USER_INSERT, input,
                            new Values(input.getStringByField("partner"), input.getStringByField("mac"),
                                    input.getStringByField("srcName"), input.getStringByField("chnCode"), input.getStringByField("albumName"),
                                    input.getStringByField("playOrder"), input.getLongByField("playLength"), input.getLongByField("timeLength"),
                                    input.getStringByField("logDate")));
                } else {
                    collector.emit(Constant.LIV_TO_USER_UPDATE, input,
                            new Values(input.getStringByField("partner"), input.getStringByField("mac"),
                                    input.getStringByField("srcName"), input.getStringByField("chnCode"), input.getStringByField("albumName"),
                                    input.getStringByField("playOrder"), input.getLongByField("playLength"), input.getStringByField("logDate")));
                }
                collector.ack(input);
            }
        }, executor);
    }

    public static void program_read_async(final Session session, BoundStatement read, final OutputCollector collector,
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
                    collector.emit(Constant.LIV_TO_PROGRAM_INSERT, input,
                            new Values(input.getStringByField("partner"), input.getStringByField("srcName"),
                                    input.getStringByField("chnCode"), input.getStringByField("albumName"),
                                    input.getStringByField("playOrder"), input.getStringByField("province"),
                                    input.getStringByField("city"), input.getLongByField("playLength"),
                                    input.getLongByField("timeLength"), input.getStringByField("logDate")));
                } else {
                    collector.emit(Constant.LIV_TO_PROGRAM_UPDATE, input,
                            new Values(input.getStringByField("partner"), input.getStringByField("srcName"),
                                    input.getStringByField("chnCode"), input.getStringByField("albumName"),
                                    input.getStringByField("playOrder"), input.getStringByField("province"),
                                    input.getStringByField("city"), input.getLongByField("playLength"),
                                    input.getStringByField("logDate")));
                }
                collector.ack(input);
            }
        }, executor);
    }

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

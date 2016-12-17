package cn.gitv.bi.userinfo.appupdate.storage;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.google.common.base.Function;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.apache.commons.lang.StringUtils;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncBack {
    private static Logger log = LoggerFactory.getLogger(AsyncBack.class);
    private static ExecutorService executor = Executors.newFixedThreadPool(3);

    public static void appVersion_read_async(final Session session, BoundStatement read,
                                             final OutputCollector collector, final Tuple input) {
        ListenableFuture<ResultSet> lf = CRUDExcuter.async_read_statement(session, read);
        ListenableFuture<String> app_v = Futures.transform(lf, new Function<ResultSet, String>() {
            public String apply(ResultSet rs) {
                Row row = rs.one();
                if (row == null) {
                    return "";
                } else {
                    return row.getString(0);
                }
            }
        }, executor);
        Futures.addCallback(app_v, new FutureCallback<String>() {
            public void onFailure(Throwable t) {
                collector.fail(input);
            }

            public void onSuccess(String appVersion) {
                if (StringUtils.isBlank(appVersion) || !input.getStringByField("app_version").equals(appVersion)) {
                    collector.emit(input, new Values(input.getStringByField("enumtype"), input.getStringByField("partner"), input.getStringByField("mac"),
                            input.getStringByField("app_version"), input.getStringByField("logtime")));
                }
                collector.ack(input);
            }
        }, executor);
    }

    public static void update_back(final Session session, String cql, final OutputCollector collector,
                                   final Tuple input) {
        ListenableFuture<ResultSet> lf = CRUDExcuter.insert_statement(session, cql);
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

package cn.gitv.bi.viscosity.casstohdfs.utils;

import com.datastax.driver.core.*;

import static cn.gitv.bi.viscosity.casstohdfs.constant.Properties.*;

public class CassandraConnection {
    private static Cluster cluster = null;
    private static Session session = null;
    private static PoolingOptions poolingOptions = null;

    static {
        poolingOptions = new PoolingOptions();
        poolingOptions.setIdleTimeoutSeconds(Integer.MAX_VALUE);
        poolingOptions.setPoolTimeoutMillis(Integer.MAX_VALUE);
        poolingOptions.setConnectionsPerHost(HostDistance.LOCAL, 30, 100);
        poolingOptions.setMaxRequestsPerConnection(HostDistance.LOCAL, 100);
        cluster = Cluster.builder().addContactPoints(CASSANDRA_HOSTS).withPort(CASSANDRA_PORT)
                .withPoolingOptions(poolingOptions)
                .withSocketOptions(new SocketOptions().setReadTimeoutMillis(Integer.MAX_VALUE)).build();
        session = cluster.connect(PROJECT_KEYSPACE);
    }

    public static void closeAll() {
        if (cluster != null) {
            cluster.close();
        }
        if (session != null) {
            session.close();
        }
    }

    public synchronized static Session getSession() {
        if (session == null) {
            if (cluster == null) {
                cluster = Cluster.builder().addContactPoints(CASSANDRA_HOSTS)
                        .withPort(CASSANDRA_PORT).withPoolingOptions(poolingOptions)
                        .withSocketOptions(new SocketOptions().setReadTimeoutMillis(Integer.MAX_VALUE)).build();
            }
            return cluster.connect(PROJECT_KEYSPACE);
        }
        if (session.isClosed()) {
            if (cluster == null) {
                cluster = Cluster.builder().addContactPoints(CASSANDRA_HOSTS)
                        .withPort(CASSANDRA_PORT).withPoolingOptions(poolingOptions)
                        .withSocketOptions(new SocketOptions().setReadTimeoutMillis(Integer.MAX_VALUE)).build();
            }
            cluster.newSession();
        }
        return session;
    }

}

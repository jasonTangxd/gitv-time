package cn.gitv.bi.userinfo.uifmaintain.utils;

import cn.gitv.bi.userinfo.uifmaintain.constant.Properties;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SocketOptions;

import static cn.gitv.bi.userinfo.uifmaintain.constant.Properties.CASSANDRA_HOSTS;
import static cn.gitv.bi.userinfo.uifmaintain.constant.Properties.CASSANDRA_PORT;

public class CassandraConnection {
	private static Cluster cluster = null;
	private static Session session = null;
	private static PoolingOptions poolingOptions = null;

	static {
		poolingOptions = new PoolingOptions();
		poolingOptions.setIdleTimeoutSeconds(10 * 60);
		poolingOptions.setPoolTimeoutMillis(10 * 1000);
		poolingOptions.setConnectionsPerHost(HostDistance.LOCAL, 30, 100);
		poolingOptions.setMaxRequestsPerConnection(HostDistance.LOCAL, 100);
		cluster = Cluster.builder().addContactPoints(CASSANDRA_HOSTS).withPort(CASSANDRA_PORT)
				.withPoolingOptions(poolingOptions)
				.withSocketOptions(new SocketOptions().setReadTimeoutMillis(60 * 1000)).build();
		session = cluster.connect(Properties.PROJECT_KEYSPACE);
	}

	public synchronized static Session getSession() {
		if (session == null) {
			if (cluster == null) {
				cluster = Cluster.builder().addContactPoints(CASSANDRA_HOSTS)
						.withPort(CASSANDRA_PORT).withPoolingOptions(poolingOptions)
						.withSocketOptions(new SocketOptions().setReadTimeoutMillis(60 * 1000)).build();
			}
			return cluster.connect(Properties.PROJECT_KEYSPACE);
		}
		if (session.isClosed()) {
			if (cluster == null) {
				cluster = Cluster.builder().addContactPoints(CASSANDRA_HOSTS)
						.withPort(CASSANDRA_PORT).withPoolingOptions(poolingOptions)
						.withSocketOptions(new SocketOptions().setReadTimeoutMillis(60 * 1000)).build();
			}
			cluster.newSession();
		}
		return session;
	}

}

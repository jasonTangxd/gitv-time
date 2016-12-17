package cn.gitv.bi.viscosity.casstohdfs.utils;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.CuratorFrameworkFactory.Builder;
import org.apache.curator.retry.ExponentialBackoffRetry;
import static cn.gitv.bi.viscosity.casstohdfs.constant.Properties.ZK_HOST_PORT;


public class CuratorTools {
	private static CuratorFramework zkclient = null;

	static {
		RetryPolicy rp = new ExponentialBackoffRetry(1000, 3);// 重试机制
		Builder builder = CuratorFrameworkFactory.builder().connectString(ZK_HOST_PORT).connectionTimeoutMs(5000)
				.sessionTimeoutMs(7000).retryPolicy(rp);
		CuratorFramework zclient = builder.build();
		zkclient = zclient;
		zkclient.start();// 放在这前面执行
	}

	public static synchronized CuratorFramework getSimpleCurator() {
		return zkclient;
	}
}

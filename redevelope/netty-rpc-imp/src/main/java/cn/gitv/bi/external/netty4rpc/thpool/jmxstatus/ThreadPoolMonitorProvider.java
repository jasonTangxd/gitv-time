package cn.gitv.bi.external.netty4rpc.thpool.jmxstatus;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.support.ConnectorServerFactoryBean;
import org.springframework.jmx.support.MBeanServerConnectionFactoryBean;
import org.springframework.jmx.support.MBeanServerFactoryBean;
import org.springframework.remoting.rmi.RmiRegistryFactoryBean;

import java.io.IOException;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import cn.gitv.bi.external.netty4rpc.netty.serverimp.MessageRecExecutor;


@Configuration
@EnableMBeanExport
@ComponentScan("cn.gitv.bi.external.netty4rpc.thpool.jmxstatus")
public class ThreadPoolMonitorProvider {
    public final static String DELIMITER = ":";
    public final static String JMX_POOL_SIZE_METHOD = "setPoolSize";
    public final static String JMX_ACTIVE_COUNT_METHOD = "setActiveCount";
    public final static String JMX_CORE_POOL_SIZE_METHOD = "setCorePoolSize";
    public final static String JMX_MAXIMUM_POOL_SIZE_METHOD = "setMaximumPoolSize";
    public final static String JMX_LARGEST_POOL_SIZE_METHOD = "setLargestPoolSize";
    public final static String JMX_TASK_COUNT_METHOD = "setTaskCount";
    public final static String JMX_COMPLETED_TASK_COUNT_METHOD = "setCompletedTaskCount";
    public static String url;

    @Bean
    public ThreadPoolStatus threadPoolStatus() {
        return new ThreadPoolStatus();
    }

    @Bean
    public MBeanServerFactoryBean mbeanServer() {
        return new MBeanServerFactoryBean();
    }

    @Bean
    public RmiRegistryFactoryBean registry() {
        return new RmiRegistryFactoryBean();
    }

    @Bean
    @DependsOn("registry")
    public ConnectorServerFactoryBean connectorServer() throws MalformedObjectNameException {
        MessageRecExecutor ref = MessageRecExecutor.getInstance();
        String ipAddr = StringUtils.isNotEmpty(ref.getServerAddress()) ? StringUtils.substringBeforeLast(ref.getServerAddress(), DELIMITER) : "localhost";
        url = "service:jmxstatus:rmi://" + ipAddr + "/jndi/rmi://" + ipAddr + ":1099/nettyrpcstatus";
        System.out.println("NettyRPC JMX MonitorURL : [" + url + "]");
        ConnectorServerFactoryBean connectorServerFactoryBean = new ConnectorServerFactoryBean();
        connectorServerFactoryBean.setObjectName("connector:name=rmi");
        connectorServerFactoryBean.setServiceUrl(url);
        return connectorServerFactoryBean;
    }

    public static void monitor(ThreadPoolStatus status) throws IOException, MalformedObjectNameException, ReflectionException, MBeanException, InstanceNotFoundException {
        MBeanServerConnectionFactoryBean mBeanServerConnectionFactoryBean = new MBeanServerConnectionFactoryBean();
        mBeanServerConnectionFactoryBean.setServiceUrl(url);
        mBeanServerConnectionFactoryBean.afterPropertiesSet();
        MBeanServerConnection connection = mBeanServerConnectionFactoryBean.getObject();
        ObjectName objectName = new ObjectName("com.newlandframework.rpc.jmxstatus:name=threadPoolStatus,enumtype=ThreadPoolStatus");

        connection.invoke(objectName, JMX_POOL_SIZE_METHOD, new Object[]{status.getPoolSize()}, new String[]{int.class.getName()});
        connection.invoke(objectName, JMX_ACTIVE_COUNT_METHOD, new Object[]{status.getActiveCount()}, new String[]{int.class.getName()});
        connection.invoke(objectName, JMX_CORE_POOL_SIZE_METHOD, new Object[]{status.getCorePoolSize()}, new String[]{int.class.getName()});
        connection.invoke(objectName, JMX_MAXIMUM_POOL_SIZE_METHOD, new Object[]{status.getMaximumPoolSize()}, new String[]{int.class.getName()});
        connection.invoke(objectName, JMX_LARGEST_POOL_SIZE_METHOD, new Object[]{status.getLargestPoolSize()}, new String[]{int.class.getName()});
        connection.invoke(objectName, JMX_TASK_COUNT_METHOD, new Object[]{status.getTaskCount()}, new String[]{long.class.getName()});
        connection.invoke(objectName, JMX_COMPLETED_TASK_COUNT_METHOD, new Object[]{status.getCompletedTaskCount()}, new String[]{long.class.getName()});
    }
}


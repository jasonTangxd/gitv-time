package cn.gitv.bi.realtime.ttl.start;

import cn.gitv.bi.realtime.ttl.thread.TriggerTask;
import cn.gitv.bi.realtime.ttl.utils.CmdOption;
import org.apache.commons.cli.*;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Kang on 2016/12/24.
 */
public class Start_Up {
    private static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String args[]) throws Exception {
        String host = null;
        String type = null;
        JedisPool compJedisPool = null;
        int port = 0;
        CommandLineParser parser = new PosixParser();
        Options options = CmdOption.buildOptions();
        CommandLine cmd = parser.parse(options, args);
        if (cmd.hasOption("h")) {
            printHelpAndExit(options);
        }
        //get cmd value
        if (cmd.hasOption("ip") && cmd.hasOption("type") && cmd.hasOption("port")) {
            host = cmd.getOptionValue("ip");
            type = cmd.getOptionValue("type");
            port = Integer.parseInt(cmd.getOptionValue("port"));
        } else {
            printHelpAndExit(options);
        }
        //init by type and check type
        switch (type) {
            case "liv":
                compJedisPool = new JedisPool("10.10.121.120", 55556);
                break;
            case "vod":
                compJedisPool = new JedisPool("10.10.121.120", 55555);
                break;
            default:
                printHelpAndExit(options);
                break;
        }
        //cn.bi.gitv.hip.parquetdemo.start listen
        JedisPool jedisPool = new JedisPool(host, port);
        jedisPool.getResource().psubscribe(new MyJedisPubSub(jedisPool, compJedisPool, executorService), "__keyevent@0__:expired");
        //System.out.println("host:" + host + "\t" + "type:" + type + "\t" + "port:" + port);
    }

    /**
     * @param options CmdOption.buildOptions()之后返回的配置完后的opt
     *                友好的在终端提示help信息
     */
    private static void printHelpAndExit(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("real-time-ttl.jar", options);
        System.exit(0);
    }
}

class MyJedisPubSub extends JedisPubSub {
    private JedisPool jedisPool;
    private JedisPool compJedisPool;
    private ExecutorService executorService;

    public MyJedisPubSub(JedisPool jedisPool, JedisPool compJedisPool, ExecutorService executorService) {
        this.compJedisPool = compJedisPool;
        this.jedisPool = jedisPool;
        this.executorService = executorService;
    }

    /**
     * @param message 订阅的频道返回的具体信息
     *                订阅"__keyevent@0__:expired"以后，每次收到消息调用的函数
     */
    @Override
    public void onPMessage(String pattern, String channel, String message) {
        executorService.submit(new TriggerTask(compJedisPool, jedisPool, message));
    }
}
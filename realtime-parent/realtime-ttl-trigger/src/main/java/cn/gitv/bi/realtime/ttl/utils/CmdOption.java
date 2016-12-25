package cn.gitv.bi.realtime.ttl.utils;

import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

/**
 * Created by Kang on 2016/12/20.
 */
public class CmdOption {
    public static Options buildOptions() {
        Options options = new Options();
        options.addOption(OptionBuilder.withArgName("type").withLongOpt("type").hasArg().withDescription("liv or vod").create("t"));
        options.addOption(OptionBuilder.withArgName("port").withLongOpt("port").hasArg().withDescription("redis的端口号").create("p"));
        options.addOption(OptionBuilder.withArgName("ip").withLongOpt("ip").hasArg().withDescription("redis的ip地址").create("i"));
        options.addOption(OptionBuilder.withLongOpt("help").withDescription("show this help").create("h"));
        return options;
    }
}

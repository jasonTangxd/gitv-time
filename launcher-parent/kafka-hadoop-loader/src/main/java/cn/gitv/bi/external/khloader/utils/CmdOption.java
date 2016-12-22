package cn.gitv.bi.external.khloader.utils;

import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

/**
 * Created by Kang on 2016/12/20.
 */
public class CmdOption {
    public static Options buildOptions() {
        Options options = new Options();
        options.addOption(OptionBuilder.withArgName("topics").withLongOpt("topics").hasArg().withDescription("kafka topics").create("t"));
        options.addOption(OptionBuilder.withArgName("groupid").withLongOpt("consumer-group").hasArg().withDescription("kafka consumer groupid").create("g"));
        options.addOption(OptionBuilder.withArgName("zk").withLongOpt("zk-connect").hasArg().withDescription("ZooKeeper connection String").create("z"));
        options.addOption(OptionBuilder.withArgName("offset").withLongOpt("offset-reset").hasArg().withDescription("Reset all offsets to either 'earliest' or 'latest'").create("o"));
        options.addOption(OptionBuilder.withArgName("compression").withLongOpt("compress-output").hasArg().withDescription("GZip output compression on|off").create("c"));
        options.addOption(OptionBuilder.withArgName("ip_address").withLongOpt("remote").hasArg().withDescription("Running on a remote hadoop node").create("r"));
        options.addOption(OptionBuilder.withLongOpt("help").withDescription("Show this help").create("h"));
        return options;
    }
}

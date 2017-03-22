package cn.gitv.bi.launcher.tohdfs.constant;

public interface MyProperties {
    String ZK_HOST_PORT = "slave7.gitv.rack2.bk:2281,slave8.gitv.rack2.bk:2281,slave9.gitv.rack2.bk:2281,slave10.gitv.rack2.bk:2281,slave11.gitv.rack2.bk:2281,master2.gitv.rack2.bk:2281,master1.gitv.rack1.bk:2281,slave1.gitv.rack1.bk:2281,slave2.gitv.rack1.bk:2281,slave3.gitv.rack1.bk:2281,slave4.gitv.rack1.bk:2281";
    String CONSUMER_TOPIC = "bi-launcher-test";
    String SPOUT_INZK = "bi_launcher";
    String[] TOPICS = {"launcher-log-100", "launcher-log-101", "launcher-log-102", "launcher-log-103", "launcher-log-104"};
    String BOOTSTRAP = "slave8.gitv.rack2.bk:9092,master2.gitv.rack2.bk:9092,master1.gitv.rack1.bk:9092,slave2.gitv.rack1.bk:9092,slave4.gitv.rack1.bk:9092";
}

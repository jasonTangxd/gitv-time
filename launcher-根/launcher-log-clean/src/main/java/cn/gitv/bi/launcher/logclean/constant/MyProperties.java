package cn.gitv.bi.launcher.logclean.constant;

public interface MyProperties {
    String ZK_HOST_PORT = "slave7.gitv.rack2.bk:2281,slave8.gitv.rack2.bk:2281,slave9.gitv.rack2.bk:2281,slave10.gitv.rack2.bk:2281,slave11.gitv.rack2.bk:2281,master2.gitv.rack2.bk:2281,master1.gitv.rack1.bk:2281,slave1.gitv.rack1.bk:2281,slave2.gitv.rack1.bk:2281,slave3.gitv.rack1.bk:2281,slave4.gitv.rack1.bk:2281";
    String[] CASSANDRA_HOSTS = {"10.10.121.138", "10.10.121.121", "10.10.121.122", "10.10.121.123", "10.10.121.139", "10.10.121.148"};
    int CASSANDRA_PORT = 9042;
    String PROJECT_KEYSPACE = "launcher";
    String CONSUMER_TOPIC = "bi-launcher-test";
    String SPOUT_INZK = "bi_launcher";
    String BOOTSTRAP = "slave8.gitv.rack2.bk:9092,master2.gitv.rack2.bk:9092,master1.gitv.rack1.bk:9092,slave2.gitv.rack1.bk:9092,slave4.gitv.rack1.bk:9092";
    String Serializer = "org.apache.kafka.common.serialization.StringSerializer";
}

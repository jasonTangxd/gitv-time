package cn.gitv.bi.viscosity.tvplay.constant;

public interface Properties {
    String ZK_HOST_PORT = "slave7.gitv.rack2.bk:2281,slave8.gitv.rack2.bk:2281,slave9.gitv.rack2.bk:2281,slave10.gitv.rack2.bk:2281,slave11.gitv.rack2.bk:2281,master2.gitv.rack2.bk:2281,master1.gitv.rack1.bk:2281,slave1.gitv.rack1.bk:2281,slave2.gitv.rack1.bk:2281,slave3.gitv.rack1.bk:2281,slave4.gitv.rack1.bk:2281";
    String[] CASSANDRA_HOSTS = {"10.10.121.138", "10.10.121.122", "10.10.121.123", "10.10.121.139", "10.10.121.148"};
    int CASSANDRA_PORT = 9042;
    String PROJECT_KEYSPACE = "viscositys";
    String CONSUMER_TOPIC = "day-filter-record";
    String SPOUT_INZK = "viscosity_spout";
}

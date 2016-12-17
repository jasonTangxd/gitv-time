package cn.gitv.bi.rtliv.usercount.constant;

public interface Properties {
    String ZK_HOST_PORT = "slave7.gitv.rack2.bk:2281,slave8.gitv.rack2.bk:2281,slave9.gitv.rack2.bk:2281,slave10.gitv.rack2.bk:2281,slave11.gitv.rack2.bk:2281,master2.gitv.rack2.bk:2281,master1.gitv.rack1.bk:2281,slave1.gitv.rack1.bk:2281,slave2.gitv.rack1.bk:2281,slave3.gitv.rack1.bk:2281,slave4.gitv.rack1.bk:2281";
    String CONSUMER_TOPIC = "log-clean-5";
    String SPOUT_INZK = "rt_liv";
    int REDIS_PORT_1 = 55556;
    int REDIS_PORT_2 = 55557;
    String REDIS_HOST_COMP = "10.10.121.120";
}

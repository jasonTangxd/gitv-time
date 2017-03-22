package cn.gitv.bi.viscosity.casstohdfs.constant;

public interface Constant {
    String PRE_CQL = "select * from viscositys.%s WHERE logdate > '%s' and logdate < '%s' ALLOW FILTERING";
    String SEPARATOR = "|";
    String PRE_HDFS_PATH = "/viscosity/%s/%s/%s/%s/%s/%s/cth_file.%s";
    String ZK_DATE_PATH = "/play_viscosity/hdfs_date/%s/%s/%s/%s";
}

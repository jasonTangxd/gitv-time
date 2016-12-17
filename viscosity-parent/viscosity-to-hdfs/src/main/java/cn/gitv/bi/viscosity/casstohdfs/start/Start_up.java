package cn.gitv.bi.viscosity.casstohdfs.start;

import cn.gitv.bi.viscosity.casstohdfs.utils.FileFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static cn.gitv.bi.viscosity.casstohdfs.constant.Constant.*;

public class Start_up {
    private static Logger logger = LoggerFactory.getLogger(Start_up.class);

    public static void main(String[] args) {
//       nohup java -jar /data/opt/script/viscosity/vtoh.jar '2016-12-05' '2016-12-07' 'vod' ''vod_viscosity_user' '2016-12-06' 'text'> nohup1.out 2>&1 &
        if (args.length != 6) {
            throw new IllegalArgumentException("启动服务输入参数不正确");
        }
        //egg:  2016-12-05
        String beginDate = args[0];
        //egg:  2016-12-07
        String endDate = args[1];
        //egg:  liv or vod
        String type = args[2];
        //egg:  vod_viscosity_user、vod_viscosity_program、liv_viscosity_user、liv_viscosity_program
        String table = args[3];
        //egg:  2016-12-06
        String excuteDate = args[4];
        //egg:  seq、text、avro
        String fileFormat = args[5];
        //egg:  2016  12  06
        String[] ymd = excuteDate.split("-");

        //egg:    select * from viscositys.liv_viscosity_user WHERE logdate > '2016-12-05' and logdate < '2016-12-07' ALLOW FILTERING
        String excuteCql = String.format(PRE_CQL, table, beginDate, endDate);
        logger.debug("excuteCql is {}", excuteCql);
        //egg:      /play_viscosity/hdfs_date/liv/liv_viscosity_user/2016-12-06
        String zkPath = String.format(ZK_DATE_PATH, fileFormat, type, table, excuteDate);
        logger.debug("zkPath is {}", zkPath);

        //egg:      /viscosity/liv/liv_viscosity_user/2016/12/06/cth_file.*
        String hdfsPath = null;
        try {
            switch (fileFormat) {
                case "seq":
                    hdfsPath = String.format(PRE_HDFS_PATH, fileFormat, type, table, ymd[0], ymd[1], ymd[2], "seq");
                    logger.debug("fileFormat is [{}],table is [{}] and hdfsPath is [{}]", fileFormat, table, hdfsPath);
                    FileFormatUtils.writeSeq(table, hdfsPath, excuteCql, excuteDate, zkPath);
                    break;
                case "text":
                    hdfsPath = String.format(PRE_HDFS_PATH, fileFormat, type, table, ymd[0], ymd[1], ymd[2], "text");
                    logger.debug("fileFormat is [{}],table is [{}] and hdfsPath is [{}]", fileFormat, table, hdfsPath);
                    FileFormatUtils.writeText(table, hdfsPath, excuteCql, excuteDate, zkPath);
                    break;
                case "avro":
                    hdfsPath = String.format(PRE_HDFS_PATH, fileFormat, type, table, ymd[0], ymd[1], ymd[2], "avro");
                    logger.debug("fileFormat is [{}],table is [{}] and hdfsPath is [{}]", fileFormat, table, hdfsPath);
                    FileFormatUtils.writeAvro(table, hdfsPath, excuteCql, excuteDate, zkPath);
                    break;
                case "parquet":
                    hdfsPath = String.format(PRE_HDFS_PATH, fileFormat, type, table, ymd[0], ymd[1], ymd[2], "parquet");
                    logger.debug("fileFormat is [{}],table is [{}] and hdfsPath is [{}]", fileFormat, table, hdfsPath);
                    FileFormatUtils.writeParquet(table, hdfsPath, excuteCql, excuteDate, zkPath);
                    break;
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }
}

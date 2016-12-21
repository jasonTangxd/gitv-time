package cn.gitv.bi.userinfo.rmconsumer.test;

import cn.gitv.bi.userinfo.rmconsumer.utils.CassandraConnection;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by Kang on 2016/12/21.
 */
public class HowMuchNull {
    public static void main(String[] args) throws IOException {
        BufferedWriter bufw = null;
        Session session = CassandraConnection.getSession();
        int count = 0;
        try {
            bufw = new BufferedWriter(new FileWriter("/Users/Kang/result.csv"));
            ResultSet rs = session.execute("SELECT mac_addr,city_name  from userinfo.user_info where partner='AH_CMCC'");
            Iterator<Row> it = rs.iterator();
            while (it.hasNext()) {
                Row row = it.next();
                StringBuffer sb = new StringBuffer();
//                String info = sb.append(row.getString("mac")).append(",")
//                        .append(row.getString("partner")).append(",").append(row.getString("chn_code")).append(row.getString("vt"))
//                        .append(row.getTimestamp("begintime")).append(",")
//                        .append(row.getTimestamp("endtime")).append(",")
//                        .append(row.getString("ip")).append(row.getString("version")).append(row.getString("albumname")).toString();
                String info = "";
                if (row.getString("city_name") == null) {
                    info = sb.append(row.getString("mac_addr")).toString();
                    bufw.write(info);
                    bufw.newLine();
                    System.out.println(info);
                    ++count;
                }
            }
        } catch (Exception e) {
            System.out.println("time out");
        } finally {
            System.out.println(count);
            bufw.close();
            System.out.println("over");
            session.close();
        }
    }

}

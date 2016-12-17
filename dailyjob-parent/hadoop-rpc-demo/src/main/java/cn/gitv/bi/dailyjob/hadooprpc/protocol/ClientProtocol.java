package cn.gitv.bi.dailyjob.hadooprpc.protocol;

import org.apache.hadoop.ipc.VersionedProtocol;

import java.io.IOException;

/**
 * Created by Kang on 2016/12/5.
 */
public interface ClientProtocol extends VersionedProtocol {
    long versionID = 1L;

    String echo(String value) throws IOException;

    int add(int v1, int v2) throws IOException;
}

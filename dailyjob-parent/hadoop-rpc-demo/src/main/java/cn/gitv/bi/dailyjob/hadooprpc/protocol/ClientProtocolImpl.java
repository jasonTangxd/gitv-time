package cn.gitv.bi.dailyjob.hadooprpc.protocol;

import org.apache.hadoop.ipc.ProtocolSignature;

import java.io.IOException;

/**
 * Created by Kang on 2016/12/5.
 */
public class ClientProtocolImpl implements ClientProtocol {
    public String echo(String value) throws IOException {
        return value;
    }

    public int add(int v1, int v2) throws IOException {
        return v1 + v2;
    }

    public long getProtocolVersion(String protocol, long clientVersion) throws IOException {
        return ClientProtocol.versionID;
    }

    public ProtocolSignature getProtocolSignature(String protocol, long clientVersion, int clientMethodsHash) throws IOException {
        return new ProtocolSignature(ClientProtocol.versionID, null);
    }
}

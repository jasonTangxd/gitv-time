package cn.gitv.bi.dailyjob.hadooprpc.client;

import cn.gitv.bi.dailyjob.hadooprpc.protocol.ClientProtocol;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.io.IOException;
import java.net.InetSocketAddress;

import static cn.gitv.bi.dailyjob.hadooprpc.constant.Constant.IP_ADDRESS;
import static cn.gitv.bi.dailyjob.hadooprpc.constant.Constant.PORT;
import static cn.gitv.bi.dailyjob.hadooprpc.protocol.ClientProtocol.versionID;


/**
 * Created by Kang on 2016/12/5.
 */
public class RPCClient {
    public static void main(String args[]) throws IOException {
        ClientProtocol proxy = (ClientProtocol) RPC.getProxy(ClientProtocol.class, versionID, new InetSocketAddress(IP_ADDRESS, PORT), new Configuration());
        int result = proxy.add(1, 4);
        String value = proxy.echo("result");
        System.out.println(value + ":" + result);
    }
}

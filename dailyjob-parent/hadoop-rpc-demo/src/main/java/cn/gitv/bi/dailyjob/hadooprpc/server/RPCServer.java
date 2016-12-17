package cn.gitv.bi.dailyjob.hadooprpc.server;

import cn.gitv.bi.dailyjob.hadooprpc.protocol.ClientProtocol;
import cn.gitv.bi.dailyjob.hadooprpc.protocol.ClientProtocolImpl;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.ipc.Server;

import java.io.IOException;

import static cn.gitv.bi.dailyjob.hadooprpc.constant.Constant.IP_ADDRESS;
import static cn.gitv.bi.dailyjob.hadooprpc.constant.Constant.PORT;

/**
 * Created by Kang on 2016/12/5.
 */
public class RPCServer {
    //server的程序需要提交jar执行,不可远程绑定server
    public static void main(String args[]) throws IOException {
        Server server = new RPC.Builder(new Configuration()).setProtocol(ClientProtocol.class).setInstance(new ClientProtocolImpl()).setBindAddress(IP_ADDRESS)
                .setPort(PORT).setNumHandlers(5).build();
        server.start();
    }
}

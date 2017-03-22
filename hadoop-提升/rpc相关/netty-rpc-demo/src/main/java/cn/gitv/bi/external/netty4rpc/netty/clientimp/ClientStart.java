package cn.gitv.bi.external.netty4rpc.netty.clientimp;

import cn.gitv.bi.external.netty4rpc.services.AddCalculate;
import cn.gitv.bi.external.netty4rpc.services.impl.AddCalculateImpl;

import java.lang.reflect.Proxy;

/**
 * Created by Kang on 2016/12/12.
 */
public class ClientStart {
    public static void main(String args[]) {
        AddCalculate addCalculate = new AddCalculateImpl();
        AddCalculate o = (AddCalculate) Proxy.newProxyInstance(addCalculate.getClass().getClassLoader(), addCalculate.getClass().getInterfaces(), new InvocationHandlerImp(addCalculate));
        System.out.println(o.add(10, 11));
    }
}

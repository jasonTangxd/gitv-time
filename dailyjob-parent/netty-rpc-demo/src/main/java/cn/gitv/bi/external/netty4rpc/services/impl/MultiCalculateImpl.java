package cn.gitv.bi.external.netty4rpc.services.impl;

import cn.gitv.bi.external.netty4rpc.services.MultiCalculate;

public class MultiCalculateImpl implements MultiCalculate {
    //两数相乘
    public int multi(int a, int b) {
        return a * b;
    }
}

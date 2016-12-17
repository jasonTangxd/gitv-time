package cn.gitv.bi.external.netty4rpc.services.impl;

import cn.gitv.bi.external.netty4rpc.services.PersonManage;
import cn.gitv.bi.external.netty4rpc.services.pojo.Person;

public class PersonManageImpl implements PersonManage {
    public int save(Person p) {
        //your business logic code here!
        System.out.println("person data[" + p + "] has save!");
        return 0;
    }
}


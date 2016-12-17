package cn.gitv.bi.external.netty4rpc.services;

import cn.gitv.bi.external.netty4rpc.services.pojo.Person;

public interface PersonManage {
    int save(Person p);
}

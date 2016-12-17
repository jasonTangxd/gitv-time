package cn.gitv.bi.external.netty4rpc.event;

import org.springframework.context.ApplicationEvent;

public class ServerStartEvent extends ApplicationEvent {
    public ServerStartEvent(Object source) {
        super(source);
    }
}


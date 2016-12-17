package cn.gitv.bi.external.netty4rpc.serialize;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public enum RpcSerializeProtocol {
    JDKSERIALIZE("jdknative"),
    KRYOSERIALIZE("kryo"),
    HESSIANSERIALIZE("hessian"),
    PROTOSTUFFSERIALIZE("protostuff");

    private String serializeProtocol;

    RpcSerializeProtocol(String serializeProtocol) {
        this.serializeProtocol = serializeProtocol;
    }

    public String toString() {
        ReflectionToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
        return ReflectionToStringBuilder.toString(this);
    }

    public String getProtocol() {
        return serializeProtocol;
    }
}

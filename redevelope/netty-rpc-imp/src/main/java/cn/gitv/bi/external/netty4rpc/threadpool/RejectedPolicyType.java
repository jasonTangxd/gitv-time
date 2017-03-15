package cn.gitv.bi.external.netty4rpc.threadpool;

public enum RejectedPolicyType {
    ABORT_POLICY("AbortPolicyImp"),
    WAIT4_POLICY("Wait4PolicyImp"),
    CALLER_RUNS_POLICY("CallerRunsPolicyImp"),
    DISCARDED_POLICY("DiscardedPolicyImp"),
    BUY_TICKET_POLICY("BuyTicketPolicyImp");

    private String value;

    RejectedPolicyType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static RejectedPolicyType byStr(String value) {
        for (RejectedPolicyType type : RejectedPolicyType.values()) {
            if (type.getValue().equalsIgnoreCase(value.trim())) {
                return type;
            }
        }
        throw new IllegalArgumentException("Mismatched RejectedPolicyType with value=" + value);
    }

    public String toString() {
        return value;
    }
}


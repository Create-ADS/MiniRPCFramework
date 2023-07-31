package online.pigeonshouse.minirpc.api.util.method;

interface ArgMatchStrategy {
    boolean match(Class<?> type, Object arg);
}
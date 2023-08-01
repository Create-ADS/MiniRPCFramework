package online.pigeonshouse.minirpc.util.method;

interface ArgMatchStrategy {
    boolean match(Class<?> type, Object arg);
}
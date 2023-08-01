package online.pigeonshouse.minirpc.thread;

public interface CallableWithCallback<R> {
    R call() throws Throwable;
}
package online.pigeonshouse.minirpc.api.thread;

public interface CallableWithCallback<R> {
    R call() throws Exception;
}
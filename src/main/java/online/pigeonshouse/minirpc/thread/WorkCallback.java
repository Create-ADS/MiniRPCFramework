package online.pigeonshouse.minirpc.thread;

public interface WorkCallback<T> {
    void onSuccess(T result);
    void onFailure(Throwable e);
    void onTimeout();
}
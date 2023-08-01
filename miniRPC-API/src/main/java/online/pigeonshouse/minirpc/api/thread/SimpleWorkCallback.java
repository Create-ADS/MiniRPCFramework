package online.pigeonshouse.minirpc.api.thread;

public class SimpleWorkCallback<T> implements WorkCallback<T>{
    @Override
    public void onSuccess(T result) {

    }

    @Override
    public void onFailure(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onTimeout() {

    }
}

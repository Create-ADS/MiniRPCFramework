package online.pigeonshouse.minirpc.thread;

public class WorkTask<T> implements Comparable<WorkTask<?>> {
    private final Runnable task;
    private final CallableWithCallback<T> callable;
    private Priority priority;
    private int count;
    private long timeout;
    private final WorkCallback<T> callback;

    public WorkTask(Runnable task, int count, Priority priority, long timeout, WorkCallback<T> callback) {
        this.task = task;
        this.callable = null;
        this.count = count;
        this.priority = priority;
        setTimeout(timeout);
        this.callback = callback;
    }

    public WorkTask(CallableWithCallback<T> callable, int count, Priority priority, long timeout, WorkCallback<T> callback) {
        this.task = null;
        this.callable = callable;
        this.count = count;
        this.priority = priority;
        setTimeout(timeout);
        this.callback = callback;
    }

    public void setTimeout(long timeout) {
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout must be positive!");
        }
        if (timeout == 0) {
            timeout = 1000 * 60 * 5;
        }
        this.timeout = timeout;
    }

    public Runnable getTask() {
        return task;
    }

    public CallableWithCallback<T> getCallable() {
        return callable;
    }
    public WorkCallback<T> getCallback() {
        return callback;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getTimeout() {
        return timeout;
    }

    @Override
    public int compareTo(WorkTask<?> other) {
        return other.priority.compareTo(this.priority);
    }
}
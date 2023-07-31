package online.pigeonshouse.minirpc.api.thread;

import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 本来API里是没有这个类的，我本来可以直接用线程池的
 * 但当我写完我才意识到这个问题
 * 没办法，我总不能删除这个类的源码吧？！
 * 我打算后续实现功能时用到，至少截止至2023/7/31，此类无作用
 */
public class WorkThread implements Runnable {
    private final PriorityQueue<WorkTask<?>> workQueue = new PriorityQueue<>();
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private final Thread workThread;
    private final static long DEFAULT_TIMEOUT = 0;

    public WorkThread() {
        workThread = new Thread(this);
        workThread.setName("WorkThread");
    }

    public WorkThread(String threadName) {
        workThread = new Thread(this);
        workThread.setName(threadName);
    }

    public boolean cancel(Runnable task) {
        synchronized (workQueue) {
            return workQueue.removeIf(workTask -> workTask.getTask().equals(task));
        }
    }

    public void start() {
        if (isRunning.get()) {
            throw new RuntimeException("WorkThread is already running!");
        }
        isRunning.set(true);
        workThread.start();
    }

    public void queueWork(Runnable action) {
        queueWork(action, Priority.NORMAL);
    }

    public void queueWork(Runnable action, Priority priority) {
        queueWork(action, priority, null);
    }

    public <T> void queueWork(Runnable action, Priority priority, WorkCallback<T> callback) {
        queueWork(action, priority, DEFAULT_TIMEOUT, callback);
    }

    public <T> void queueWork(Runnable action, Priority priority, long timeout, WorkCallback<T> callback) {
        queueWork(1, action, priority, timeout, callback);
    }

    /**
     * 将任务加入队列
     *
     * @param count 任务重复执行的次数
     * @param action 任务
     * @param priority 任务优先级
     * @param timeout 任务超时时间
     * @param callback 任务回调
     * @param <T> 任务返回值类型
     */
    public <T> void queueWork(int count, Runnable action, Priority priority, long timeout, WorkCallback<T> callback) {
        synchronized (workQueue) {
            workQueue.add(new WorkTask<>(action, count, priority, timeout, callback));
            workQueue.notifyAll();
        }
    }

    public <R> void queueWork(CallableWithCallback<R> callable, WorkCallback<R> callback) {
        queueWork(callable, Priority.NORMAL, callback);
    }

    public <R> void queueWork(CallableWithCallback<R> callable, Priority priority, WorkCallback<R> callback) {
        queueWork(callable, priority, DEFAULT_TIMEOUT, callback);
    }

    public <R> void queueWork(CallableWithCallback<R> callable, Priority priority, long timeout, WorkCallback<R> callback) {
        queueWork(1, callable, priority, timeout, callback);
    }

    /**
     * 将任务加入队列
     *
     * @param count 任务重复执行的次数
     * @param callable 任务 （回调）
     * @param priority 任务优先级
     * @param timeout 任务超时时间
     * @param callback 任务回调
     * @param <R> 任务返回值类型
     */
    public <R> void queueWork(int count, CallableWithCallback<R> callable, Priority priority, long timeout, WorkCallback<R> callback) {
        synchronized (workQueue) {
            workQueue.add(new WorkTask<>(callable, count, priority, timeout, callback));
            workQueue.notifyAll();
        }
    }

    @Override
    public void run() {
        while (isRunning.get()) {
            try {
                WorkTask<?> task = null;
                synchronized (workQueue) {
                    if (workQueue.isEmpty()) {
                        workQueue.wait();
                    } else {
                        task = workQueue.poll();
                    }
                }

                if (task != null) {
                    executeTask(task);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * result是为了解决CallableWithCallback的问题
     */
    private Object result = null;

    /**
     * 执行任务
     */
    private <T> void executeTask(WorkTask<T> task) {
        try {
            if (task.getCount() > 0) {
                Thread taskThread;
                if (task.getTask() != null) {
                    taskThread = new Thread(task.getTask());
                } else if (task.getCallable() != null) {
                    taskThread = new Thread(() -> {
                        try {
                            result = task.getCallable().call();
                        } catch (Exception e) {
                            e.printStackTrace();
                            task.getCallback().onFailure(e);
                        }
                    });
                } else {
                    throw new RuntimeException("Task is null!");
                }
                taskThread.setDaemon(true);
                taskThread.start();
                taskThread.join(task.getTimeout());

                if (taskThread.isAlive()) {
                    task.setCount(0);
                    System.err.println("Task " + task.getTask().toString() + " timeout! Task will not be executed!");
                    taskThread.interrupt();
                    task.getCallback().onTimeout();
                } else {
                    task.setCount(task.getCount() - 1);
                    if (task.getCount() > 0) {
                        synchronized (workQueue) {
                            workQueue.add(task);
                        }
                        workQueue.notifyAll();
                    }
                    if (task.getCallback() != null){
                        task.getCallback().onSuccess((T) result);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (task.getCallback() != null){
                task.getCallback().onFailure(e);
            }
        }finally {
            result = null;
        }
    }

    public void stop() {
        isRunning.set(false);
        workThread.interrupt();
    }

    public boolean isRunning() {
        return isRunning.get();
    }
}

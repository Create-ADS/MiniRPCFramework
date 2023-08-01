package online.pigeonshouse.minirpc.service.rpc;

import online.pigeonshouse.minirpc.framwork.Message;

public interface RpcInvokeHelper {
    void invoke(String methodName, Object... params);

    void invoke(Message message);

    <R> R invokeForResult(Class<R> r,String methodName, Object... params);

    <R> R invokeForResult(Class<R> r,Message message);
}
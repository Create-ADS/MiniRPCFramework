package online.pigeonshouse.minirpc.api.service.rpc;

import online.pigeonshouse.minirpc.api.framwork.Message;
import online.pigeonshouse.minirpc.api.service.MiniObject;

public interface RpcInvokeHelper {
    void invoke(String methodName, Object... params);

    void invoke(Message message);

    <R> R invokeForResult(Class<R> r,String methodName, Object... params);

    <R> R invokeForResult(Class<R> r,Message message);
}
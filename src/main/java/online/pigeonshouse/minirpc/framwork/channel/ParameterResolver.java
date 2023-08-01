package online.pigeonshouse.minirpc.framwork.channel;

import online.pigeonshouse.minirpc.framwork.request.RemoteCallRequest;
import online.pigeonshouse.minirpc.framwork.pool.SimpleObjectPool;

public interface ParameterResolver {
    Object[] resolve(RemoteCallRequest request, SimpleObjectPool pool);
}
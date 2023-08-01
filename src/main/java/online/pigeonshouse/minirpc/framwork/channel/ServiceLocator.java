package online.pigeonshouse.minirpc.framwork.channel;

import online.pigeonshouse.minirpc.framwork.request.RemoteCallRequest;
import online.pigeonshouse.minirpc.framwork.pool.SimpleObjectPool;
import online.pigeonshouse.minirpc.register.ServerServiceProperty;

public interface ServiceLocator {
    ServerServiceProperty lookup(RemoteCallRequest request, SimpleObjectPool pool);
}
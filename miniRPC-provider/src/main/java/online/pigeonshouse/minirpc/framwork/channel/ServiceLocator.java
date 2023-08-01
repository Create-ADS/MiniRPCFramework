package online.pigeonshouse.minirpc.framwork.channel;

import io.netty.channel.Channel;
import online.pigeonshouse.minirpc.api.framwork.request.RemoteCallRequest;
import online.pigeonshouse.minirpc.framwork.pool.SimpleObjectPool;
import online.pigeonshouse.minirpc.register.ServerServiceProperty;

public interface ServiceLocator {
    ServerServiceProperty lookup(RemoteCallRequest request, SimpleObjectPool pool);
}
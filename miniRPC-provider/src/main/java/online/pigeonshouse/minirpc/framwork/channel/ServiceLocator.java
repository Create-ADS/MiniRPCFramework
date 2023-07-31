package online.pigeonshouse.minirpc.framwork.channel;

import online.pigeonshouse.minirpc.api.framwork.request.RemoteCallRequest;
import online.pigeonshouse.minirpc.register.ServerServiceProperty;

public interface ServiceLocator {
    ServerServiceProperty lookup(RemoteCallRequest request);
}
package online.pigeonshouse.minirpc.framwork.channel;

import online.pigeonshouse.minirpc.framwork.request.RemoteCallRequest;
import online.pigeonshouse.minirpc.framwork.pool.SimpleObjectPool;
import online.pigeonshouse.minirpc.register.ServerServiceProperty;
import online.pigeonshouse.minirpc.register.ServiceRegisterManage;

public class CacheServiceLocator implements ServiceLocator {
    @Override
    public ServerServiceProperty lookup(RemoteCallRequest request, SimpleObjectPool pool) {
        ServiceRegisterManage instance = pool.get("serviceRegisterManage", ServiceRegisterManage.class);
        String serviceGroup = request.getGroup();
        String serviceName = request.getServiceName();
        String serviceVersion = request.getServiceVersion();
        return instance.lookup(serviceGroup, serviceName, serviceVersion);
    }
}
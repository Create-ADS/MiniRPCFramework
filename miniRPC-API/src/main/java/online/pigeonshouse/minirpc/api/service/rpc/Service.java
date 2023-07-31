package online.pigeonshouse.minirpc.api.service.rpc;

public abstract class Service {
    public final RpcInvokeHelper rpcInvokeHelper;
    public Service(ServiceInvoke invoke, Class<? extends Service> clazz) {
        RpcInvokeHelperImpl invokeHelper = new RpcInvokeHelperImpl(invoke, clazz);
        rpcInvokeHelper = invokeHelper;
        invokeHelper.getMethodCache().registerClass(clazz);
    }
}

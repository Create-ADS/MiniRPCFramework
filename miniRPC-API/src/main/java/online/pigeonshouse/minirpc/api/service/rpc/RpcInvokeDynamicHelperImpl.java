package online.pigeonshouse.minirpc.api.service.rpc;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import online.pigeonshouse.minirpc.api.framwork.request.RemoteCallRequest;
import online.pigeonshouse.minirpc.api.framwork.response.MessageResponse;
import online.pigeonshouse.minirpc.api.service.MiniObject;
import online.pigeonshouse.minirpc.api.util.MiniUtil;

public class RpcInvokeDynamicHelperImpl extends AbstractRpcInvokeHelper{
    private final String serviceName;
    private final String serviceVersion;
    private String serviceGroup = "";

    public RpcInvokeDynamicHelperImpl(ServiceInvoke invoke, String serviceName, String serviceVersion) {
        super(invoke);
        if (StrUtil.isEmpty(serviceName) || StrUtil.isEmpty(serviceVersion)) {
            throw new RuntimeException("serviceName, serviceVersion can not be empty");
        }
        this.serviceName = serviceName;
        this.serviceVersion = serviceVersion;
    }

    public RpcInvokeDynamicHelperImpl(ServiceInvoke invoke ,String serviceName, String serviceVersion, String serviceGroup) {
        this(invoke , serviceName, serviceVersion);
        if (StrUtil.isEmpty(serviceGroup)) {
            throw new RuntimeException("serviceGroup can not be empty");
        }
        this.serviceGroup = serviceGroup;
    }

    @Override
    protected RemoteCallRequest buildRequest(String method, Object[] params) {
        return RemoteCallRequestBuilder.create()
                .setServiceName(serviceName)
                .setServiceVersion(serviceVersion)
                .setGroup(serviceGroup)
                .setMethodName(method)
                .setParams(params)
                .setStatic(false)
                .build();
    }

    @Override
    protected <R> R parseResponse(MessageResponse response, Class<R> t) {
        return MiniUtil.parseResponse(response, t);
    }
}

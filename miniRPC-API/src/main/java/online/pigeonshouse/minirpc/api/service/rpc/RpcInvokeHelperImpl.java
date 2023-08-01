package online.pigeonshouse.minirpc.api.service.rpc;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ClassUtil;
import online.pigeonshouse.minirpc.api.MiniService;
import online.pigeonshouse.minirpc.api.framwork.request.RemoteCallRequest;
import online.pigeonshouse.minirpc.api.framwork.response.MessageResponse;
import online.pigeonshouse.minirpc.api.service.MiniObject;
import online.pigeonshouse.minirpc.api.util.MiniUtil;

public class RpcInvokeHelperImpl extends AbstractRpcInvokeHelper {
    protected MethodCache methodCache;
    protected Class<? extends Service> myClass;

    public RpcInvokeHelperImpl(ServiceInvoke invoke, Class<? extends Service> myClass) {
        super(invoke);
        methodCache = new MethodCache();
        this.myClass = myClass;
    }

    @Override
    protected RemoteCallRequest buildRequest(String method, Object[] params) {
        if (!AnnotationUtil.hasAnnotation(myClass,MiniService.class)){
            throw new RuntimeException("The class must be annotated with @MiniService");
        }
        MiniService miniService = AnnotationUtil.getAnnotation(myClass, MiniService.class);
        return RemoteCallRequestBuilder.create()
                .setServiceName(miniService.name())
                .setServiceVersion(miniService.version())
                .setGroup(miniService.group())
                .setMethodName(method)
                .setParams(params)
                .setStatic(false)
                .build();
    }

    @Override
    protected <R> R parseResponse(MessageResponse response, Class<R> t) {
        return MiniUtil.parseResponse(response, t);
    }

    public MethodCache getMethodCache() {
        return methodCache;
    }
}
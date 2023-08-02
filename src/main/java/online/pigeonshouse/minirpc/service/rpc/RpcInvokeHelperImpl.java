package online.pigeonshouse.minirpc.service.rpc;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.IdUtil;
import online.pigeonshouse.minirpc.MiniService;
import online.pigeonshouse.minirpc.framwork.Message;
import online.pigeonshouse.minirpc.framwork.request.RemoteCallRequest;
import online.pigeonshouse.minirpc.framwork.response.MessageResponse;
import online.pigeonshouse.minirpc.framwork.response.RemoteCallResponse;
import online.pigeonshouse.minirpc.service.PublicService;
import online.pigeonshouse.minirpc.util.MiniUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RpcInvokeHelperImpl extends AbstractRpcInvokeHelper {
    protected MethodCache methodCache;
    protected Class<? extends Service> myClass;
    private Map<String, Long> methodTimeoutMap = new HashMap<>();

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
        Method thisMethod = methodCache.getMethod(method, params);
        if (thisMethod == null) {
            throw new RuntimeException("The method is not found");
        }
        PublicService publicService = AnnotationUtil.getAnnotation(thisMethod, PublicService.class);
        long timeout;
        if (publicService.isWait()) {
            timeout = -1;
        }else {
            timeout = publicService.timeout();
        }
        MiniService miniService = AnnotationUtil.getAnnotation(myClass, MiniService.class);
        RemoteCallRequest callRequest = RemoteCallRequestBuilder.create()
                .setServiceName(miniService.name())
                .setServiceVersion(miniService.version())
                .setGroup(miniService.group())
                .setMethodName(method)
                .setParams(params)
                .setStatic(false)
                .build();
        methodTimeoutMap.put(callRequest.getSessionId(), timeout);
        return callRequest;
    }

    @Override
    protected MessageResponse getResponse(String requestId) {
        Long timeOut = methodTimeoutMap.get(requestId);
        Long aLong = timeOut == null ? DEFAULT_TIMEOUT : timeOut;
        return invoke.getResult(requestId, new Object(), aLong);
    }

    @Override
    protected <R> R parseResponse(MessageResponse response, Class<R> t) {
        return MiniUtil.parseResponse(response, t);
    }

    public MethodCache getMethodCache() {
        return methodCache;
    }
}

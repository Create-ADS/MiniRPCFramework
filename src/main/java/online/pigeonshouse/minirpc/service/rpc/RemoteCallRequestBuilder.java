package online.pigeonshouse.minirpc.service.rpc;

import cn.hutool.core.util.IdUtil;
import online.pigeonshouse.minirpc.framwork.Parameter;
import online.pigeonshouse.minirpc.framwork.request.RemoteCallRequest;
import online.pigeonshouse.minirpc.util.MiniUtil;

public class RemoteCallRequestBuilder {
    private String serviceName;
    private String group = "";
    private String serviceVersion;
    private String methodName;
    private Object[] params;
    private boolean isStatic;
    private String sessionId;

    public static RemoteCallRequestBuilder create() {
        return new RemoteCallRequestBuilder();
    }

    public RemoteCallRequestBuilder setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public RemoteCallRequestBuilder setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
        return this;
    }

    public RemoteCallRequestBuilder setMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public RemoteCallRequestBuilder setParams(Object[] params) {
        this.params = params;
        return this;
    }

    public RemoteCallRequestBuilder setStatic(boolean isStatic) {
        this.isStatic = isStatic;
        return this;
    }

    public RemoteCallRequestBuilder setGroup(String group) {
        this.group = group;
        return this;
    }

    public RemoteCallRequest build() {
        if (isStatic) {
            return buildStaticRequest();
        } else {
            return buildInstanceRequest();
        }
    }

    private RemoteCallRequest buildInstanceRequest() {
        inspect();
        sessionId = serviceName + "&" + serviceVersion + "&" + methodName + "&" + IdUtil.fastSimpleUUID();
        Parameter[] parameters = MiniUtil.toParameters(params);
        if (parameters.length == 0){
            return new RemoteCallRequest(sessionId, serviceName, group,serviceVersion, false, methodName);
        }
        return new RemoteCallRequest(sessionId, serviceName, group, serviceVersion, false, methodName, parameters);
    }

    private RemoteCallRequest buildStaticRequest() {
        if (sessionId == null){
            throw new RuntimeException("sessionId is null");
        }
        inspect();
        Parameter[] parameters = MiniUtil.toParameters(params);
        if (parameters.length == 0){
            return new RemoteCallRequest(sessionId, serviceName, group,serviceVersion, methodName);
        }
        return new RemoteCallRequest(sessionId, serviceName, group,serviceVersion, methodName, parameters);
    }

    private void inspect() {
        if (methodName == null){
            throw new RuntimeException("methodName is null");
        }
        if (serviceName == null){
            throw new RuntimeException("serviceName is null");
        }
        if (serviceVersion == null){
            throw new RuntimeException("serviceVersion is null");
        }
    }
}
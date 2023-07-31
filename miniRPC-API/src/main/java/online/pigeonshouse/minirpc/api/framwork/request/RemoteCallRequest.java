package online.pigeonshouse.minirpc.api.framwork.request;

import lombok.Data;
import online.pigeonshouse.minirpc.api.framwork.MessageFactory;
import online.pigeonshouse.minirpc.api.framwork.Parameter;

import java.util.Arrays;
import java.util.List;

@Data
public class RemoteCallRequest extends MessageRequest {
    public static final MessageFactory MESSAGE_TYPE = new MessageFactory(51);

    private String serviceName;
    private String serviceVersion;
    private String methodName;
    private List<Parameter> parameters;
    private boolean isStatic;
    private String sessionId;
    private String group;

    public RemoteCallRequest() {
    }

    public RemoteCallRequest(String sessionId, String serviceName, String group, String serviceVersion, String methodName, List<Parameter> parameters) {
        this.sessionId = sessionId;
        this.serviceName = serviceName;
        this.serviceVersion = serviceVersion;
        this.methodName = methodName;
        this.group = group;
        this.parameters = parameters;
        this.isStatic = true;
    }

    public RemoteCallRequest(String sessionId, String serviceName, String group, String serviceVersion, String methodName, Parameter... parameters) {
        this(sessionId, serviceName, group, serviceVersion, methodName, Arrays.asList(parameters));
    }

    public RemoteCallRequest(String sessionId, String serviceName, String group,String serviceVersion, boolean isStatic, String methodName, List<Parameter> parameters) {
        this(sessionId, serviceName, group, serviceVersion, methodName, parameters);
        this.isStatic = isStatic;
    }

    public RemoteCallRequest(String sessionId, String serviceName, String group,String serviceVersion, boolean isStatic, String methodName, Parameter... parameters) {
        this(sessionId, serviceName, group, serviceVersion, isStatic, methodName, Arrays.asList(parameters));
    }

    @Override
    public Integer getType() {
        return MESSAGE_TYPE.getType();
    }
}
package online.pigeonshouse.minirpc.service.rpc;

import online.pigeonshouse.minirpc.framwork.Message;
import online.pigeonshouse.minirpc.framwork.request.RemoteCallRequest;
import online.pigeonshouse.minirpc.framwork.response.MessageResponse;
import online.pigeonshouse.minirpc.framwork.response.RemoteCallResponse;

public abstract class AbstractRpcInvokeHelper implements RpcInvokeHelper {
    private final ServiceInvoke invoke;

    public AbstractRpcInvokeHelper(ServiceInvoke invoke) {
        this.invoke = invoke;
    }

    protected abstract RemoteCallRequest buildRequest(String method, Object[] params);

    protected abstract <R> R parseResponse(MessageResponse response, Class<R> t);

    @Override
    public void invoke(String methodName, Object... params) {
        RemoteCallRequest request = buildRequest(methodName, params);
        invoke(request);
    }

    @Override
    public void invoke(Message message) {
        String requestId = sendMessage(message);
        MessageResponse response = getResponse(requestId);
        if (response.getException() != null) {
            throw new RuntimeException(response.getException());
        }
    }

    @Override
    public <R> R invokeForResult(Class<R> t,String methodName, Object... params) {
        RemoteCallRequest request = buildRequest(methodName, params);
        return invokeForResult(t, request);
    }

    @Override
    public <R> R invokeForResult(Class<R> t,Message message) {
        String requestId = sendMessage(message);
        return parseResponse(getResponse(requestId), t);
    }

    private String sendMessage(Message msg) {
        invoke.send(msg);
        return msg.getSessionId();
    }

    private MessageResponse getResponse(String requestId) {
        return invoke.getResult(requestId, new RemoteCallResponse(), 50000);
    }
}
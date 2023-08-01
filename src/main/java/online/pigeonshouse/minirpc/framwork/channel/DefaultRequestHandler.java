package online.pigeonshouse.minirpc.framwork.channel;

import cn.hutool.core.util.IdUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import online.pigeonshouse.minirpc.framwork.request.RemoteCallRequest;
import online.pigeonshouse.minirpc.framwork.response.RemoteCallResponse;
import online.pigeonshouse.minirpc.service.MiniObject;
import online.pigeonshouse.minirpc.util.MiniUtil;
import online.pigeonshouse.minirpc.framwork.pool.SimpleObjectPool;
import online.pigeonshouse.minirpc.framwork.pool.UserSessionPoolManager;
import online.pigeonshouse.minirpc.register.ServerServiceProperty;

import java.util.Arrays;

@Data
public class DefaultRequestHandler implements RequestHandler {
    private ServiceLocator serviceLocator;
    private ParameterResolver parameterResolver;

    @Override
    public void handleRequest(RemoteCallRequest request, ChannelHandlerContext ctx) throws Throwable {
        UserSessionPoolManager poolManager = ProtocolSelectorHandler.getUserSessionPoolManager();
        SimpleObjectPool simpleObjectPool = poolManager.get(ctx.channel());
        ServerServiceProperty lookup = serviceLocator.lookup(request, simpleObjectPool);
        Object[] parameters = parameterResolver.resolve(request, simpleObjectPool);
        System.out.println(Arrays.toString(parameters));
        Object result = lookup.invokeMethod(request.getMethodName(), parameters);
        RemoteCallResponse response = buildResponse(result, request.getSessionId(), simpleObjectPool);
        ctx.writeAndFlush(response);
    }

    private RemoteCallResponse buildResponse(Object result, String sessionId, SimpleObjectPool pool) {
        RemoteCallResponse response = new RemoteCallResponse();
        MiniObject wrap = MiniUtil.wrap(result);
        if (wrap != null) {
            response.setSessionId(sessionId);
            response.setResult(wrap);
        } else {
            response.setSessionId(sessionId);
            String className = result.getClass().getName();
            String uid = IdUtil.fastSimpleUUID();
            String key = className + "&" + uid;
            pool.put(key, result);
            response.setValue(new MiniObject(className, uid));
        }
        return response;
    }
}
package online.pigeonshouse.minirpc.framwork.channel.inbound;

import cn.hutool.core.util.IdUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import online.pigeonshouse.minirpc.api.MiniType;
import online.pigeonshouse.minirpc.api.framwork.Parameter;
import online.pigeonshouse.minirpc.api.framwork.request.RemoteCallRequest;
import online.pigeonshouse.minirpc.api.framwork.response.RemoteCallResponse;
import online.pigeonshouse.minirpc.api.service.MiniObject;
import online.pigeonshouse.minirpc.api.util.MiniUtil;
import online.pigeonshouse.minirpc.framwork.channel.ProtocolSelectorHandler;
import online.pigeonshouse.minirpc.framwork.pool.SimpleObjectPool;
import online.pigeonshouse.minirpc.framwork.pool.UserSessionPoolManager;
import online.pigeonshouse.minirpc.register.ServerServiceProperty;
import online.pigeonshouse.minirpc.register.ServiceRegisterManage;

import java.util.List;

@ChannelHandler.Sharable
public class RemoteCallRequestChannelInboundHandler extends SimpleChannelInboundHandler<RemoteCallRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RemoteCallRequest request) throws Exception {
        ServiceRegisterManage instance = ServiceRegisterManage.getInstance();
        ServerServiceProperty service = instance.getService(request.getGroup(), request.getServiceName(), request.getServiceVersion());
        if (service == null) {
            ctx.channel().writeAndFlush(new RemoteCallResponse(new RuntimeException("No service found"), request.getSessionId())).sync();
            return;
        }
        UserSessionPoolManager poolManager = ProtocolSelectorHandler.getUserSessionPoolManager();
        SimpleObjectPool simpleObjectPool = poolManager.get(ctx.channel());
        List<Parameter> parameters = request.getParameters();
        Parameter[] array = parameters.toArray(new Parameter[0]);
        MiniObject[] miniObjects = MiniUtil.toObjects(array);
        Object invoke;
        try {
            Object[] objects = MiniUtil.unwrapAsList(miniObjects);
            for (int i = 0; i < objects.length; i++) {
                if (objects[i] instanceof MiniObject) {
                    MiniObject miniObject = (MiniObject) objects[i];
                    if (miniObject.getTYPE() != MiniType.MINI_OBJ) {
                        throw new RuntimeException("The parameter type is not MiniObject");
                    }
                    Object obj = simpleObjectPool.get(miniObject.getClassName() + "&" + miniObject.getUUID());
                    if (obj == null) {
                        throw new RuntimeException("No object found");
                    }
                    objects[i] = obj;
                }
            }
            invoke = service.invokeMethod(request.getMethodName(), objects);
        }catch (Throwable e){
            e.printStackTrace();
            ctx.channel().writeAndFlush(new RemoteCallResponse(e, request.getSessionId())).sync();
            return;
        }
        MiniObject wrap = MiniUtil.wrap(invoke);
        if (wrap == null) {
            String name = invoke.getClass().getName();
            String id = IdUtil.fastSimpleUUID();
            String key = name + "&" + id;
            simpleObjectPool.put(key, invoke);
            wrap = new MiniObject(name, id);
        }
        ctx.channel().writeAndFlush(new RemoteCallResponse(wrap, request.getSessionId())).sync();
    }
}

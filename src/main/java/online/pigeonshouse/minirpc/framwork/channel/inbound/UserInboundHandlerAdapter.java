package online.pigeonshouse.minirpc.framwork.channel.inbound;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import online.pigeonshouse.minirpc.NettyServerBoot;
import online.pigeonshouse.minirpc.framwork.channel.ProtocolSelectorHandler;
import online.pigeonshouse.minirpc.framwork.pool.SimpleObjectPool;
import online.pigeonshouse.minirpc.framwork.pool.UserSessionPoolManager;
import online.pigeonshouse.minirpc.register.ServiceRegisterFactory;
import online.pigeonshouse.minirpc.register.ServiceRegisterManage;

public class UserInboundHandlerAdapter extends ChannelInboundHandlerAdapter {
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        UserSessionPoolManager poolManager = ProtocolSelectorHandler.getUserSessionPoolManager();
        SimpleObjectPool put = poolManager.put(ctx.channel());
        poolManager.getGlobalSessionPool().put("clientChannel", ctx.channel());
        put.put("serviceRegisterManage", new ServiceRegisterManage(ctx.channel()));
        NettyServerBoot.scanServicePackage(ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        UserSessionPoolManager poolManager = ProtocolSelectorHandler.getUserSessionPoolManager();
        poolManager.remove(ctx.channel());
        super.handlerRemoved(ctx);
    }
}

package online.pigeonshouse.minirpc.framwork.channel.inbound;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import online.pigeonshouse.minirpc.framwork.channel.ProtocolSelectorHandler;
import online.pigeonshouse.minirpc.framwork.pool.UserSessionPoolManager;

public class UserInboundHandlerAdapter extends ChannelInboundHandlerAdapter {
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        UserSessionPoolManager poolManager = ProtocolSelectorHandler.getUserSessionPoolManager();
        poolManager.put(ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        UserSessionPoolManager poolManager = ProtocolSelectorHandler.getUserSessionPoolManager();
        poolManager.remove(ctx.channel());
        super.handlerRemoved(ctx);
    }
}

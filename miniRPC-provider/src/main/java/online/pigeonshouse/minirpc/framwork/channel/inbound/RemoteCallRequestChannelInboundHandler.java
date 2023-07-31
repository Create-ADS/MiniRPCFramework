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
import online.pigeonshouse.minirpc.framwork.channel.FilterChain;
import online.pigeonshouse.minirpc.framwork.channel.ProtocolSelectorHandler;
import online.pigeonshouse.minirpc.framwork.channel.RequestHandler;
import online.pigeonshouse.minirpc.framwork.channel.RequestHandlerFactory;
import online.pigeonshouse.minirpc.framwork.pool.SimpleObjectPool;
import online.pigeonshouse.minirpc.framwork.pool.UserSessionPoolManager;
import online.pigeonshouse.minirpc.register.ServerServiceProperty;
import online.pigeonshouse.minirpc.register.ServiceRegisterManage;

import java.util.List;

@ChannelHandler.Sharable
public class RemoteCallRequestChannelInboundHandler extends SimpleChannelInboundHandler<RemoteCallRequest> {
    private RequestHandler handler;
    private FilterChain filterChain;

    public RemoteCallRequestChannelInboundHandler() {
        this.handler = RequestHandlerFactory.createHandler();
        this.filterChain = new FilterChain();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, RemoteCallRequest request) {
        filterChain.doFilter(request, ctx);
        try {
            handler.handleRequest(request, ctx);
        } catch (Throwable e) {
            ctx.channel().writeAndFlush(new RemoteCallResponse(e, request.getSessionId()));
        }
    }
}

package online.pigeonshouse.minirpc.framwork.channel.inbound;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import online.pigeonshouse.minirpc.framwork.request.RemoteCallRequest;
import online.pigeonshouse.minirpc.framwork.response.RemoteCallResponse;
import online.pigeonshouse.minirpc.framwork.channel.FilterChain;
import online.pigeonshouse.minirpc.framwork.channel.RequestHandler;
import online.pigeonshouse.minirpc.framwork.channel.RequestHandlerFactory;

@ChannelHandler.Sharable
public class RemoteCallRequestChannelInboundHandler extends SimpleChannelInboundHandler<RemoteCallRequest> {
    private RequestHandler handler;
    private FilterChain filterChain;

    public RemoteCallRequestChannelInboundHandler() {
        this.handler = RequestHandlerFactory.createHandler();
        this.filterChain = new FilterChain();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, RemoteCallRequest request) throws Exception {
        filterChain.doFilter(request, ctx);
        try {
            handler.handleRequest(request, ctx);
        } catch (Throwable e) {
            e.printStackTrace();
            ctx.channel().writeAndFlush(new RemoteCallResponse(e, request.getSessionId())).sync();
        }
    }
}

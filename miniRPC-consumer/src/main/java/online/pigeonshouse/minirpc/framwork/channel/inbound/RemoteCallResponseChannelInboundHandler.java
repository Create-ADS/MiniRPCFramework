package online.pigeonshouse.minirpc.framwork.channel.inbound;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import online.pigeonshouse.minirpc.ClientStartTest;
import online.pigeonshouse.minirpc.api.framwork.response.RemoteCallResponse;

@ChannelHandler.Sharable
public class RemoteCallResponseChannelInboundHandler extends SimpleChannelInboundHandler<RemoteCallResponse> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RemoteCallResponse response) throws Exception {
        ClientStartTest.getClientBoot().getClientInvoke().putResult(response.getSessionId(), response);
    }
}

package online.pigeonshouse.minirpc.framwork.channel.inbound;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import online.pigeonshouse.minirpc.api.framwork.request.AllocationServiceRequest;

@ChannelHandler.Sharable
public class AllocationRequestChannelInboundHandler extends SimpleChannelInboundHandler<AllocationServiceRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AllocationServiceRequest request) throws Exception {

    }
}

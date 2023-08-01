package online.pigeonshouse.minirpc.framwork.channel;

import io.netty.channel.ChannelHandlerContext;
import online.pigeonshouse.minirpc.framwork.request.RemoteCallRequest;

public interface Filter {
    void doFilter(RemoteCallRequest request, ChannelHandlerContext ctx);
}

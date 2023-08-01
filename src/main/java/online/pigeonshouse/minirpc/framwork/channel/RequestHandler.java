package online.pigeonshouse.minirpc.framwork.channel;

import io.netty.channel.ChannelHandlerContext;
import online.pigeonshouse.minirpc.framwork.request.RemoteCallRequest;

public interface RequestHandler {
    void handleRequest(RemoteCallRequest request, ChannelHandlerContext ctx) throws Throwable;
}
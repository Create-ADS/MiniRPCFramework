package online.pigeonshouse.minirpc.framwork.channel;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import online.pigeonshouse.minirpc.framwork.channel.protocol.ProtocolPackageFilter;
import online.pigeonshouse.minirpc.framwork.pool.UserSessionPoolManager;

import java.util.ArrayList;
import java.util.List;

@ChannelHandler.Sharable
public class ProtocolSelectorHandler extends MessageToMessageCodec<ByteBuf, ByteBuf> {
    private static final List<ProtocolPackageFilter> filter = new ArrayList<>();
    private static final UserSessionPoolManager USER_SESSION_POOL_MANAGER = new UserSessionPoolManager();

    public static UserSessionPoolManager getUserSessionPoolManager() {
        return USER_SESSION_POOL_MANAGER;
    }

    public static void addProtocolPackageFilter(ProtocolPackageFilter protocolPackageFilter) {
        filter.add(protocolPackageFilter);
    }

    public static void removeProtocolPackageFilter(ProtocolPackageFilter protocolPackageFilter) {
        filter.remove(protocolPackageFilter);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {

    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        for (ProtocolPackageFilter protocolPackageFilter : filter) {
            if (protocolPackageFilter.isProtocolPackage(byteBuf)) {
                try {
                    protocolPackageFilter.handleProtocolPackage(ctx.pipeline());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        ctx.pipeline().remove(this);
        ByteBuf buf = ctx.alloc().buffer();
        buf.writeBytes(byteBuf);
        ctx.fireChannelRead(buf);
    }
}
package online.pigeonshouse.minirpc.framwork.channel.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelPipeline;
import online.pigeonshouse.minirpc.framwork.MessageCodecSharable;

public class WebSocketProtocolPackageFilter extends ProtocolPackageFilter{
    @Override
    public boolean isProtocolPackage(ByteBuf byteBuf) {
        return false;
    }

    @Override
    public void handleProtocolPackage(ChannelPipeline pipeline) {
    }
}

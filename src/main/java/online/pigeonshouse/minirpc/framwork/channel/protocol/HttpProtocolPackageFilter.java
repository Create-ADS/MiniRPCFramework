package online.pigeonshouse.minirpc.framwork.channel.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelPipeline;
import online.pigeonshouse.minirpc.framwork.MessageCodecSharable;

public class HttpProtocolPackageFilter extends ProtocolPackageFilter {
    @Override
    public boolean isProtocolPackage(ByteBuf byteBuf) {
        String line = readFirstLine(byteBuf);
        if (line == null) {
            return false;
        }
        if (line.matches("^GET /.* HTTP/1.1$") || line.matches("^POST /.* HTTP/1.1$")) {
            return true;
        }
        return false;
    }

    @Override
    public void handleProtocolPackage(ChannelPipeline pipeline) {
    }
}

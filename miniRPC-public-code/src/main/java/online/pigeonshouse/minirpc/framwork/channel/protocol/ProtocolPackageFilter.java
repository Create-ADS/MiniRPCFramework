package online.pigeonshouse.minirpc.framwork.channel.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelPipeline;

public abstract class ProtocolPackageFilter {
    public abstract boolean isProtocolPackage(ByteBuf byteBuf);
    public abstract void handleProtocolPackage(ChannelPipeline pipeline);

    protected boolean startsWith(ByteBuf buf, byte[] prefix) {
        if (buf.readableBytes() < prefix.length) {
            return false;
        }

        for (int i = 0; i < prefix.length; i++) {
            if (buf.getByte(buf.readerIndex() + i) != prefix[i]) {
                return false;
            }
        }
        return true;
    }

    protected String readFirstLine(ByteBuf buf) {
        int index = buf.indexOf(buf.readerIndex(), buf.writerIndex(), (byte) '\n');
        if (index == -1) {
            return null;
        }
        int length = index - buf.readerIndex() - 1;
        byte[] bytes = new byte[length];
        buf.getBytes(buf.readerIndex(), bytes);
        return new String(bytes);
    }
}

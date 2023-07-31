package online.pigeonshouse.minirpc.framwork;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import static online.pigeonshouse.minirpc.framwork.MessageCodecSharable.HEADER_LENGTH;

public class MessageLengthFieldDecoder extends LengthFieldBasedFrameDecoder {
    public MessageLengthFieldDecoder(){
        this(500 * 1024,HEADER_LENGTH - 4,4,0,0);
    }

    public MessageLengthFieldDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }
}

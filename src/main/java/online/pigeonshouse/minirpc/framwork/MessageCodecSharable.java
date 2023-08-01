package online.pigeonshouse.minirpc.framwork;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.util.internal.StringUtil;
import online.pigeonshouse.minirpc.framwork.serialize.Serialize;
import online.pigeonshouse.minirpc.framwork.serialize.SerializeFactory;

import java.util.HashMap;
import java.util.List;

/**
 * 网络协议
 */
@ChannelHandler.Sharable
public class MessageCodecSharable extends MessageToMessageCodec<ByteBuf, Message> {
    public static final byte[] MODS = "minirpc".getBytes();
    public static final byte[] VERSION = "0.0.1".getBytes();
    private static final HashMap<String,Byte> custom = new HashMap<>();
    public static final byte JSON_SERIALIZE = 1;
    public static final int HEADER_LENGTH = MODS.length + VERSION.length + 2 + 1 + 1 + 4 + 4;


    public static void addCustom(Class<? extends Message> obj,Byte type){
        custom.put(obj.getName(),type);
    }

    public static void remove(Class<? extends Message> obj){
        custom.remove(obj.getName());
    }

    @Override
    protected void encode(ChannelHandlerContext ct, Message message, List<Object> list) throws Exception {
        Byte serializeByte = custom.get(message.getClass().getName());
        byte type = serializeByte == null ? JSON_SERIALIZE : serializeByte;
        ByteBuf buf = ct.alloc().buffer();
        buf.writeBytes(MODS);
        buf.writeBytes(VERSION);
        buf.writeChar('\n');
        buf.writeByte(type);
        buf.writeByte(0xff);
        byte[] serialize = SerializeFactory.get(type).serialize(message);
        buf.writeInt(message.getType());
        buf.writeInt(serialize.length);
        buf.writeBytes(serialize);
        list.add(buf);
    }

    @Override
    protected void decode(ChannelHandlerContext ct, ByteBuf byteBuf, List<Object> list) throws Exception {
        byte[] mods = new byte[MODS.length];
        byte[] version = new byte[VERSION.length];
        byteBuf.readBytes(mods);
        byteBuf.readBytes(version);
        byteBuf.skipBytes(2);
        Serialize serialize = SerializeFactory.get(byteBuf.readByte());
        if (serialize == null){
            ct.channel().close();
            throw new RuntimeException("协议包版本不一致，已主动断开连接！");
        }
        byteBuf.readByte();
        Class<? extends Message> o = MessageFactory.valueOf(byteBuf.readInt());
        int length = byteBuf.readInt();
        byte[] obj = new byte[length];
        byteBuf.readBytes(obj,0,length);
        Message deserialize = serialize.deserialize(o, obj);
        list.add(deserialize);
    }

    public static void log(ByteBuf b){
        int length = b.readableBytes();
        int rows =length / 16 + (length % 5 == 0 ? 0 : 1) + 4;
        StringBuilder builder = new StringBuilder(rows * 80 * 2)
                .append("read index:").append(b.readerIndex())
                .append(" write index:").append(b.writerIndex())
                .append(" capacity:").append(b.capacity())
                .append(StringUtil.NEWLINE);
        ByteBufUtil.appendPrettyHexDump(builder,b);
        System.out.println(builder);
    }
}

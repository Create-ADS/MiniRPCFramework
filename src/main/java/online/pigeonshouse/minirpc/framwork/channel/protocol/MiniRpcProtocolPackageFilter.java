package online.pigeonshouse.minirpc.framwork.channel.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import online.pigeonshouse.minirpc.framwork.MessageCodecSharable;
import online.pigeonshouse.minirpc.framwork.MessageLengthFieldDecoder;

import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

public class MiniRpcProtocolPackageFilter extends ProtocolPackageFilter {
    private static final byte[] MINIRPC_PREFIX = MessageCodecSharable.MODS;
    private static ChannelPipeline pipeline;
    private static CopyOnWriteArrayList<ChannelHandler> line = new CopyOnWriteArrayList<>();
    private static CopyOnWriteArrayList<Class<? extends ChannelHandler>>  synLine = new CopyOnWriteArrayList<>();

    @Override
    public boolean isProtocolPackage(ByteBuf byteBuf) {
        return startsWith(byteBuf, MINIRPC_PREFIX);
    }

    @Override
    public void handleProtocolPackage(ChannelPipeline pipeline) {
        MiniRpcProtocolPackageFilter.pipeline = pipeline;
        pipeline.addLast(new MessageLengthFieldDecoder())
                .addLast(new MessageCodecSharable());
        for (Class<? extends ChannelHandler> o : synLine){
            try {
                pipeline.addLast(o.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                System.err.println("load pipeline error: " + o.getName());
                e.printStackTrace();
            }
        }

        for (ChannelHandler handler : line) {
            try{
                pipeline.addLast(handler);
            }catch (Exception e){
                System.err.println("load pipeline error: " + handler.getClass().getName());
                e.printStackTrace();
            }
        }
    }

    public static void addList(ChannelHandler... handlers){
        if (pipeline != null){
            pipeline.addLast(handlers);
        }
        line.addAll(Arrays.asList(handlers));
    }

    public static void addSynList(Class<? extends  ChannelHandler>... obj){
        if (pipeline != null){
            for (Class<? extends ChannelHandler> p : obj){
                try {
                    pipeline.addLast(p.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        synLine.addAll(Arrays.asList(obj));
    }
}

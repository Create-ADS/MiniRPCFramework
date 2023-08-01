package online.pigeonshouse.minirpc;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import online.pigeonshouse.minirpc.framwork.Message;
import online.pigeonshouse.minirpc.framwork.NettyBoot;
import online.pigeonshouse.minirpc.service.rpc.ServiceInvoke;
import online.pigeonshouse.minirpc.framwork.channel.inbound.RemoteCallResponseChannelInboundHandler;
import online.pigeonshouse.minirpc.framwork.channel.protocol.MiniRpcProtocolPackageFilter;

public class NettyClientBoot implements NettyBoot {
    private final NioEventLoopGroup WORKER;
    private final Bootstrap BOOTSTRAP;
    private ChannelFuture channelFuture;
    private final ServiceInvoke CLIENT_INVOKE;

    public ServiceInvoke getClientInvoke() {
        return CLIENT_INVOKE;
    }

    public NettyClientBoot(Integer time) {
        WORKER = new NioEventLoopGroup();
        BOOTSTRAP = new Bootstrap();
        CLIENT_INVOKE = new ServiceInvoke(this);
        registerHandler();
        BOOTSTRAP.group(WORKER)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,time)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        new MiniRpcProtocolPackageFilter().handleProtocolPackage(pipeline);
                    }
                });
    }

    public ChannelFuture start(String host,Integer port) throws InterruptedException {
        return channelFuture = BOOTSTRAP.connect(host, port);
    }

    public void closeEventSyn() throws InterruptedException {
        channelFuture.channel().closeFuture().sync();
    }

    public void close(){
        // 判断是否已经关闭
        if (channelFuture != null){
            channelFuture.channel().close();
        }
        WORKER.shutdownGracefully();
    }

    public void registerHandler(){
        MiniRpcProtocolPackageFilter.addList(new RemoteCallResponseChannelInboundHandler());
    }

    @Override
    public void sendMessage(Message message) throws InterruptedException {
        channelFuture.channel().writeAndFlush(message).sync();
    }
}

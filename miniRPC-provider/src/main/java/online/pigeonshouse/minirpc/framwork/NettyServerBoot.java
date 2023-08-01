package online.pigeonshouse.minirpc.framwork;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import online.pigeonshouse.minirpc.api.framwork.Message;
import online.pigeonshouse.minirpc.api.framwork.NettyBoot;
import online.pigeonshouse.minirpc.framwork.channel.ProtocolSelectorHandler;
import online.pigeonshouse.minirpc.framwork.channel.inbound.RemoteCallRequestChannelInboundHandler;
import online.pigeonshouse.minirpc.framwork.channel.inbound.UserInboundHandlerAdapter;
import online.pigeonshouse.minirpc.framwork.channel.protocol.HttpProtocolPackageFilter;
import online.pigeonshouse.minirpc.framwork.channel.protocol.MiniRpcProtocolPackageFilter;
import online.pigeonshouse.minirpc.framwork.channel.protocol.WebSocketProtocolPackageFilter;
import online.pigeonshouse.minirpc.register.ServiceRegisterFactory;

public class NettyServerBoot implements NettyBoot {
    private final NioEventLoopGroup BOSS,WOKER;
    private final ServerBootstrap SERVER_BOOTSTRAP;
    private ChannelFuture channelFuture;

    public NettyServerBoot() {
        BOSS = new NioEventLoopGroup();
        WOKER = new NioEventLoopGroup();
        SERVER_BOOTSTRAP = new ServerBootstrap();
        ProtocolSelectorHandler.addProtocolPackageFilter(new HttpProtocolPackageFilter());
        ProtocolSelectorHandler.addProtocolPackageFilter(new WebSocketProtocolPackageFilter());
        ProtocolSelectorHandler.addProtocolPackageFilter(new MiniRpcProtocolPackageFilter());
        registerHandler();
        SERVER_BOOTSTRAP.group(BOSS,WOKER)
                .childOption(ChannelOption.TCP_NODELAY,true)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ProtocolSelectorHandler());
    }

    public ChannelFuture start(Integer port) throws InterruptedException {
        return channelFuture = SERVER_BOOTSTRAP.bind(port).sync();
    }

    public void closeEventSyn() throws InterruptedException {
        channelFuture.channel().closeFuture().sync();
    }

    public void close(){
        if (channelFuture != null){
            channelFuture.channel().close();
        }
        BOSS.shutdownGracefully();
        WOKER.shutdownGracefully();
    }

    public void sendMessage(Message msg) throws InterruptedException {
        throw new UnsupportedOperationException("Server can't send message");
    }

    public void registerHandler(){
        MiniRpcProtocolPackageFilter.addSynList(UserInboundHandlerAdapter.class);
        MiniRpcProtocolPackageFilter.addList(new RemoteCallRequestChannelInboundHandler());
    }
}

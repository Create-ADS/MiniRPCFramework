package online.pigeonshouse.minirpc;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import online.pigeonshouse.minirpc.framwork.Message;
import online.pigeonshouse.minirpc.framwork.NettyBoot;
import online.pigeonshouse.minirpc.framwork.channel.ProtocolSelectorHandler;
import online.pigeonshouse.minirpc.framwork.channel.inbound.RemoteCallRequestChannelInboundHandler;
import online.pigeonshouse.minirpc.framwork.channel.inbound.UserInboundHandlerAdapter;
import online.pigeonshouse.minirpc.framwork.channel.protocol.HttpProtocolPackageFilter;
import online.pigeonshouse.minirpc.framwork.channel.protocol.MiniRpcProtocolPackageFilter;
import online.pigeonshouse.minirpc.framwork.channel.protocol.WebSocketProtocolPackageFilter;
import online.pigeonshouse.minirpc.framwork.pool.SimpleObjectPool;
import online.pigeonshouse.minirpc.framwork.pool.UserSessionPoolManager;
import online.pigeonshouse.minirpc.register.ServiceRegisterManage;

import java.util.ArrayList;
import java.util.List;

public class NettyServerBoot implements NettyBoot {
    private final NioEventLoopGroup BOSS,WOKER;
    private final ServerBootstrap SERVER_BOOTSTRAP;
    private ChannelFuture channelFuture;
    private static final List<String> servicePackageNames = new ArrayList<>();

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

    public static void scanServicePackage(String packageName){
        if (servicePackageNames.contains(packageName)){
            return;
        }
        servicePackageNames.add(packageName);
        UserSessionPoolManager poolManager = ProtocolSelectorHandler.getUserSessionPoolManager();
        List<Object> objectList = poolManager.getGlobalSessionPool().getValues("clientChannel");
        if (objectList == null || objectList.size() == 0){
            return;
        }
        for (Object o : objectList) {
            SimpleObjectPool objectPool = poolManager.get((Channel) o);
            if (objectPool == null){
                continue;
            }
            ServiceRegisterManage serviceRegisterManage = (ServiceRegisterManage) objectPool.get("serviceRegisterManage");
            serviceRegisterManage.scanServicePackage(packageName);
        }
    }

    public static void scanServicePackage(Channel channel){
        UserSessionPoolManager poolManager = ProtocolSelectorHandler.getUserSessionPoolManager();
        SimpleObjectPool objectPool = poolManager.get(channel);
        if (objectPool == null){
            return;
        }
        ServiceRegisterManage serviceRegisterManage = (ServiceRegisterManage) objectPool.get("serviceRegisterManage");
        for (String packageName : servicePackageNames) {
            serviceRegisterManage.scanServicePackage(packageName);
        }
    }

    public void registerHandler(){
        MiniRpcProtocolPackageFilter.addSynList(UserInboundHandlerAdapter.class);
        MiniRpcProtocolPackageFilter.addList(new RemoteCallRequestChannelInboundHandler());
    }
}

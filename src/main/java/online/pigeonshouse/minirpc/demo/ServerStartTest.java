package online.pigeonshouse.minirpc.demo;

import online.pigeonshouse.minirpc.NettyServerBoot;
import online.pigeonshouse.minirpc.framwork.Initialization;

public class ServerStartTest {
    public static void main(String[] args) {
        try {
            Initialization.init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 注册服务
        NettyServerBoot.scanServicePackage("online.pigeonshouse.minirpc.demo.service.impl");
        // 创建服务器
        NettyServerBoot serverBoot = new NettyServerBoot();
        try {
            // 启动服务器
            serverBoot.start(25565);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

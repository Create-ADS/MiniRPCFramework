package online.pigeonshouse.minirpc;

import online.pigeonshouse.minirpc.framwork.Initialization;
import online.pigeonshouse.minirpc.framwork.NettyServerBoot;

public class ServerStartTest {
    public static void main(String[] args) {
        try {
            Initialization.init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        NettyServerBoot serverBoot = new NettyServerBoot();
        try {
            serverBoot.start(25565);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

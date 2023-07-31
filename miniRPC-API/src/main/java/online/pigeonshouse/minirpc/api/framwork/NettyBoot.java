package online.pigeonshouse.minirpc.api.framwork;

public interface NettyBoot {
    void sendMessage(Message msg) throws InterruptedException;
}

package online.pigeonshouse.minirpc.framwork;

public interface NettyBoot {
    void sendMessage(Message msg) throws InterruptedException;
}

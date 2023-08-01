package online.pigeonshouse.minirpc.service.impl;

import io.netty.channel.Channel;
import online.pigeonshouse.minirpc.api.MiniService;
import online.pigeonshouse.minirpc.api.framwork.NettyBoot;
import online.pigeonshouse.minirpc.api.service.PublicService;
import online.pigeonshouse.minirpc.api.thread.ResponseFilterListener;
import online.pigeonshouse.minirpc.framwork.pool.SimpleObjectPool;
import online.pigeonshouse.minirpc.service.ServerService;
import online.pigeonshouse.minirpc.service.Test;

@MiniService(name = "HelloService", version = "1.0")
public class HelloService extends ServerService {
    public HelloService(SimpleObjectPool pool, Channel boot) {
        super(pool , boot);
    }

    @PublicService
    public String sayHello(String name) {
        return "Hello, " + name;
    }

    @PublicService
    public String sayHello(String name, int age) {
        return "Hello, " + name + ", you are " + age + " years old.";
    }

    @PublicService
    public Test sayHello(){
        return new Test();
    }

    @PublicService
    public String sayHello(Test test){
        return test.getString();
    }

    @PublicService(isWait = true)
    public void sayHello(ResponseFilterListener listener){
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                callCallback(listener.getSessionId(), new Object[]{"你好，这是一条回调消息！"});
            }
        }).start();
    }
}

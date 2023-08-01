package online.pigeonshouse.minirpc;

import cn.hutool.core.util.IdUtil;
import online.pigeonshouse.minirpc.api.service.MiniObject;
import online.pigeonshouse.minirpc.api.service.rpc.RpcInvokeDynamicHelperImpl;
import online.pigeonshouse.minirpc.api.thread.ResponseFilter;
import online.pigeonshouse.minirpc.api.thread.ResponseFilterListener;
import online.pigeonshouse.minirpc.framwork.Hello;
import online.pigeonshouse.minirpc.api.service.PublicService;
import online.pigeonshouse.minirpc.api.service.rpc.ServiceInvoke;
import online.pigeonshouse.minirpc.framwork.Initialization;
import online.pigeonshouse.minirpc.framwork.NettyClientBoot;

public class ClientStartTest {
    private static NettyClientBoot clientBoot;

    public static NettyClientBoot getClientBoot() {
        return clientBoot;
    }

    public static void main(String[] args) throws InterruptedException {
        try {
            Initialization.init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        clientBoot = new NettyClientBoot(5000);
        try {
            clientBoot.start("127.0.0.1", 25565);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // 动态调用
        RpcInvokeDynamicHelperImpl helloService = new RpcInvokeDynamicHelperImpl(clientBoot.getClientInvoke() ,"HelloService", "1.0");
        MiniObject test = helloService.invokeForResult(MiniObject.class, "sayHello");
        String result = helloService.invokeForResult(String.class, "sayHello",
                test);
        System.out.println(result);
        String result1 = helloService.invokeForResult(String.class, "sayHello",
                "Mike", 18);
        System.out.println(result1);
        Hello hello = new Hello(clientBoot.getClientInvoke());
        String result2 = hello.sayHello("world");
        System.out.println(result2);
        String result3 = hello.sayHello("Mike", 18);
        System.out.println(result3);

        ServiceInvoke clientInvoke = clientBoot.getClientInvoke();
        ResponseFilter responseFilter = clientInvoke.getResponseFilter();
        String sessionId = IdUtil.fastSimpleUUID();
        ResponseFilterListener listener = new ResponseFilterListener(sessionId, String.class) {
            @Override
            public boolean isResponseFiltered(String sessionId) {
                return this.sessionId.equals(sessionId);
            }

            @Override
            public void onResponseFiltered(Object... restParams) throws Throwable {
                System.out.println("Response filtered: " + restParams[0]);
            }
        };
        responseFilter.addResponseFilter(listener);
        helloService.invoke("sayHello", listener);
    }
}

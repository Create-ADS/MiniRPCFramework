package online.pigeonshouse.minirpc.demo;

import cn.hutool.core.util.IdUtil;
import online.pigeonshouse.minirpc.demo.service.Test;
import online.pigeonshouse.minirpc.framwork.Initialization;
import online.pigeonshouse.minirpc.service.MiniObject;
import online.pigeonshouse.minirpc.service.rpc.RpcInvokeDynamicHelperImpl;
import online.pigeonshouse.minirpc.thread.ResponseFilter;
import online.pigeonshouse.minirpc.thread.ResponseFilterListener;
import online.pigeonshouse.minirpc.demo.service.Hello;
import online.pigeonshouse.minirpc.service.rpc.ServiceInvoke;
import online.pigeonshouse.minirpc.NettyClientBoot;

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
        RpcInvokeDynamicHelperImpl helloService = new RpcInvokeDynamicHelperImpl(clientBoot.getClientInvoke() ,"HelloService", "1.0");
        MiniObject test = helloService.invokeForResult(MiniObject.class, "sayHello");
        String result = helloService.invokeForResult(String.class, "sayHello",
                test);
        System.out.println(result);
        String result1 = helloService.invokeForResult(String.class, "sayHello",
                "Mike", 18);
        System.out.println(result1);
        Hello hello = new Hello(clientBoot.getClientInvoke());
        Test test1 = hello.sayHello();
        System.out.println(hello.sayHello(test1));
        String result3 = hello.sayHello("Mike", 18);
        System.out.println(result3);

        ServiceInvoke clientInvoke = clientBoot.getClientInvoke();
        ResponseFilter responseFilter = clientInvoke.getResponseFilter();
        String sessionId = IdUtil.fastSimpleUUID();
        ResponseFilterListener listener = new ResponseFilterListener(sessionId, String.class) {
            @Override
            public boolean isResponseFiltered(String sessionId) {
                System.out.println("Response filtered: " + sessionId);
                return this.sessionId.equals(sessionId);
            }

            @Override
            public void onResponseFiltered(Object... restParams) throws Throwable {
                System.out.println("Response filtered: " + restParams[0]);
            }
        };
        responseFilter.addResponseFilter(listener);
        //[ResponseFilterListener(sessionId=5743d4d0358c46748c826648005cb21b, paramsType=null)]
//        helloService.invoke("sayHello", listener);
        //[ResponseFilterListener(sessionId=83b89f9c4b64430c9bd9ca125b4f2fb9, paramsType=null)]
        hello.sayHello(listener);
        System.out.println(123);
    }
}

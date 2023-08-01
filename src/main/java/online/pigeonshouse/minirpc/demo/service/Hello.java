package online.pigeonshouse.minirpc.demo.service;

import online.pigeonshouse.minirpc.MiniService;
import online.pigeonshouse.minirpc.service.PublicService;
import online.pigeonshouse.minirpc.service.rpc.Service;
import online.pigeonshouse.minirpc.service.rpc.ServiceInvoke;

@MiniService(name = "HelloService",version = "1.0")
public class Hello extends Service {
    public Hello(ServiceInvoke invoke) {
        super(invoke ,Hello.class);
    }

    @PublicService
    public String sayHello(String name, int age) {
        return rpcInvokeHelper.invokeForResult(String.class, "sayHello", name, age);
    }

    @PublicService
    public String sayHello(String name) {
        return rpcInvokeHelper.invokeForResult(String.class, "sayHello", name);
    }
}

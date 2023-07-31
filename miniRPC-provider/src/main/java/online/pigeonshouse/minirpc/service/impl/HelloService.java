package online.pigeonshouse.minirpc.service.impl;

import online.pigeonshouse.minirpc.api.MiniService;
import online.pigeonshouse.minirpc.api.service.PublicService;
import online.pigeonshouse.minirpc.service.ServerService;
import online.pigeonshouse.minirpc.service.Test;

@MiniService(name = "HelloService", version = "1.0")
public class HelloService implements ServerService {
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
}

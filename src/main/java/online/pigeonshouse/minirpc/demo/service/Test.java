package online.pigeonshouse.minirpc.demo.service;

import online.pigeonshouse.minirpc.service.MiniElement;
import online.pigeonshouse.minirpc.service.MiniObject;

public class Test extends MiniObject {

    public Test() {
        super(Test.class.getName(), null);
    }

    public Test(String sessionID) {
        super(Test.class.getName(), sessionID);
    }

    public String getString(){
        return "test";
    }
}

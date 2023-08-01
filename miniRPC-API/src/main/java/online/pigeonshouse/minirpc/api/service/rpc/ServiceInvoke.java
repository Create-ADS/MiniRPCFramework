package online.pigeonshouse.minirpc.api.service.rpc;

import online.pigeonshouse.minirpc.api.framwork.Message;
import online.pigeonshouse.minirpc.api.framwork.NettyBoot;
import online.pigeonshouse.minirpc.api.framwork.response.RemoteCallResponse;
import online.pigeonshouse.minirpc.api.service.PublicService;
import online.pigeonshouse.minirpc.api.thread.ResponseFilter;

import java.util.HashMap;
import java.util.Map;

public class ServiceInvoke {
    private final NettyBoot boot;
    private final Map<String, RemoteCallResponse> resultCache = new HashMap<>();
    private final Map<String, Object> waitObject = new HashMap<>();
    private final ResponseFilter responseFilter = new ResponseFilter();

    public ServiceInvoke(NettyBoot boot) {
        this.boot = boot;
    }

    public ResponseFilter getResponseFilter() {
        return responseFilter;
    }

    public void send(Message message) {
        try {
            boot.sendMessage(message);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void putResult(String uuid, RemoteCallResponse object) {
        responseFilter.filter(object);
        resultCache.put(uuid, object);
        Object waitObject = this.waitObject.remove(uuid);
        if (waitObject != null) {
            synchronized (waitObject) {
                waitObject.notify();
            }
        }
    }

    public RemoteCallResponse getResult(String uuid, Object waitObject, long timeout) {
        RemoteCallResponse object = resultCache.get(uuid);
        if (object == null) {
            this.waitObject.put(uuid, waitObject);
            synchronized (waitObject) {
                try {
                    waitObject.wait(timeout);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            object = resultCache.get(uuid);
        }
        return object;
    }
}

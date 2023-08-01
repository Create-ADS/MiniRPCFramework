package online.pigeonshouse.minirpc.api.thread;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import online.pigeonshouse.minirpc.api.MiniType;
import online.pigeonshouse.minirpc.api.framwork.response.RemoteCallResponse;
import online.pigeonshouse.minirpc.api.service.MiniObject;
import online.pigeonshouse.minirpc.api.util.MiniUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ResponseFilter extends WorkThread {
    private final List<ResponseFilterListener> listeners = new ArrayList<>();

    public ResponseFilter() {
        super("ResponseFilter");
        start();
    }

    public void addResponseFilter(ResponseFilterListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeResponseFilter(ResponseFilterListener listener) {
        listeners.remove(listener);
    }

    public void filter(RemoteCallResponse response) {
        MiniObject value = response.getValue();
        if (value == null) {
            response.getException().printStackTrace();
            return;
        }
        if (value.getTYPE() != MiniType.MINI_CALLBACK) {
            return;
        }
        Gson gson = new Gson();
        String string = value.getValue().toString();
        MiniObject[] miniObjects = gson.fromJson(string, MiniObject[].class);
        Object[] objects = MiniUtil.unwrapAsList(miniObjects);
        for (ResponseFilterListener listener : listeners) {
            if (listener.isResponseFiltered(response.getSessionId())) {
                queueWork(() -> listener.handle(objects), new SimpleWorkCallback<>());
            }
        }
    }
}

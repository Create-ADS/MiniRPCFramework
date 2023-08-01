package online.pigeonshouse.minirpc.service;

import cn.hutool.core.util.IdUtil;
import io.netty.channel.Channel;
import online.pigeonshouse.minirpc.MiniType;
import online.pigeonshouse.minirpc.framwork.response.RemoteCallResponse;
import online.pigeonshouse.minirpc.util.MiniUtil;
import online.pigeonshouse.minirpc.framwork.pool.SimpleObjectPool;

import java.util.Arrays;

public abstract class ServerService {
    protected Channel boot;
    protected SimpleObjectPool pool;

    public ServerService(SimpleObjectPool pool, Channel boot) {
        this.boot = boot;
        this.pool = pool;
    }

    public void callCallback(String sessionId, Object[] args) {
        MiniObject[] objects = MiniUtil.wrapAsList(args);
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] == null){
                Object arg = args[i];
                pool.put(arg.getClass().getName() + "&" + IdUtil.fastSimpleUUID(), arg);
                objects[i] = new MiniObject(arg.getClass().getName(), IdUtil.fastSimpleUUID());
            }
        }
        try {
            RemoteCallResponse msg = new RemoteCallResponse(new MiniObject(MiniType.MINI_CALLBACK, Arrays.asList(objects)), sessionId);
            boot.writeAndFlush(msg).sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

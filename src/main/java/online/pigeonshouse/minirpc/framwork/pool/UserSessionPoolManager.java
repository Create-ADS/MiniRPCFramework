package online.pigeonshouse.minirpc.framwork.pool;

import cn.hutool.core.map.TableMap;
import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

public class UserSessionPoolManager {
    private Map<Channel, SimpleObjectPool> userSessionPools = new HashMap<>();
    private TableMap<Object, Object> globalSessionPool = new TableMap<>();

    public SimpleObjectPool put(Channel channel) {
        SimpleObjectPool pool = new SimpleObjectPool();
        userSessionPools.put(channel, pool);
        return pool;
    }

    public SimpleObjectPool get(Channel channel) {
        return userSessionPools.get(channel);
    }

    public void remove(Channel channel) {
        userSessionPools.get(channel).clear();
        userSessionPools.remove(channel);
    }

    public TableMap<Object,Object> getGlobalSessionPool() {
        return globalSessionPool;
    }
}
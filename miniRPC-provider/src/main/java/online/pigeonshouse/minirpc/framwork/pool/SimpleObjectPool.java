package online.pigeonshouse.minirpc.framwork.pool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleObjectPool {
    private List<PoolListener> listeners = new ArrayList<>();
    private Map<Object, Object> pool = new HashMap<>();

    public void put(Object key, Object obj) {
        Object oldObj = pool.get(key);
        pool.put(key, obj);
        if (oldObj == null) {
            for (PoolListener listener : listeners) {
                listener.onObjectAdded(key, obj);
            }
        } else {
            for (PoolListener listener : listeners) {
                listener.onObjectUpdated(key, oldObj, obj);
            }
        }
    }

    public Object get(Object key) {
        Object obj = pool.get(key);
        for (PoolListener listener : listeners) {
            listener.onObjectGet(key, obj);
        }
        return obj;
    }

    public <T> T get(Object key, Class<T> clazz) {
        Object obj = get(key);
        return clazz.cast(obj);
    }

    public void remove(Object key) {
        Object obj = pool.remove(key);
        for (PoolListener listener : listeners) {
            listener.onObjectRemoved(key, obj);
        }
    }

    public void clear() {
        pool.clear();
    }

    public void addListener(PoolListener listener) {
        listeners.add(listener);
    }

    public void removeListener(PoolListener listener) {
        listeners.remove(listener);
    }
}

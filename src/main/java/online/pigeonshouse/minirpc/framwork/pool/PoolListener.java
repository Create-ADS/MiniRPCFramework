package online.pigeonshouse.minirpc.framwork.pool;

public interface PoolListener {
  void onObjectAdded(Object key, Object obj);
  void onObjectGet(Object key, Object obj);
  void onObjectUpdated(Object key, Object oldObj, Object newObj);
  void onObjectRemoved(Object key, Object obj);
}
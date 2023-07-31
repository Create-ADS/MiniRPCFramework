package online.pigeonshouse.minirpc.framwork.pool;

public interface PoolListener {
  void onObjectAdded(String key, Object obj);
  void onObjectGet(String key, Object obj);
  void onObjectUpdated(String key, Object oldObj, Object newObj);
  void onObjectRemoved(String key, Object obj);
}
package online.pigeonshouse.minirpc.framwork.serialize;

public interface Serialize {
    <T>T deserialize(Class<T> clazz, byte[] bytes);
    byte[] serialize(Object o);
}

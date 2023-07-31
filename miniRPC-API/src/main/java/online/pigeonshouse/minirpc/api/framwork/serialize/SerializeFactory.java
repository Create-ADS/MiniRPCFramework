package online.pigeonshouse.minirpc.api.framwork.serialize;

import java.util.HashMap;
import java.util.Map;

public class SerializeFactory {
    private static final Map<Byte,Serialize> MAP = new HashMap<>();

    public static void register(Byte i ,Serialize serialize){
        MAP.put(i,serialize);
    }

    public static Serialize get(Byte i){
        return MAP.get(i);
    }
}

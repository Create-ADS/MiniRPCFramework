package online.pigeonshouse.minirpc.api.framwork.encryption;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EncryptionStandardFactory {
    private static final Map<String, EncryptionStandard> MAP = new ConcurrentHashMap<>();

    public static boolean put(String type, EncryptionStandard encryptionStandard){
        if (MAP.containsKey(type)) {
            return false;
        }
        MAP.put(type, encryptionStandard);
        return true;
    }

    public static EncryptionStandard get(String type){
        return MAP.get(type);
    }
}

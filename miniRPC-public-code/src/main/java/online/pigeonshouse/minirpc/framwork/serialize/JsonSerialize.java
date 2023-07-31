package online.pigeonshouse.minirpc.framwork.serialize;

import com.google.gson.Gson;
import online.pigeonshouse.minirpc.api.framwork.encryption.EncryptionStandard;
import online.pigeonshouse.minirpc.api.framwork.encryption.EncryptionStandardFactory;
import online.pigeonshouse.minirpc.api.framwork.serialize.Serialize;

import java.nio.charset.StandardCharsets;

public class JsonSerialize implements Serialize {
    private final Gson json = new Gson();
    private final EncryptionStandard AES = EncryptionStandardFactory.get("AES");

    @Override
    public byte[] serialize(Object o) {
        String s = json.toJson(o);
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        try {
            return AES.encrypt(bytes);
        } catch (Exception e) {
            throw new RuntimeException("序列化失败！",e);
        }
    }

    @Override
    public <T> T deserialize(Class<T> aClass, byte[] bytes) {
        try {
            bytes = AES.decryptToCharArray(bytes);
        } catch (Exception e) {
            throw new RuntimeException("反序列化失败！",e);
        }
        String s = new String(bytes, StandardCharsets.UTF_8);
        return json.fromJson(s,aClass);
    }
}

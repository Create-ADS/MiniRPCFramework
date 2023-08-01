package online.pigeonshouse.minirpc.framwork;

import online.pigeonshouse.minirpc.framwork.encryption.EncryptionStandardFactory;
import online.pigeonshouse.minirpc.framwork.request.RemoteCallRequest;
import online.pigeonshouse.minirpc.framwork.response.RemoteCallResponse;
import online.pigeonshouse.minirpc.framwork.serialize.SerializeFactory;
import online.pigeonshouse.minirpc.framwork.encryption.AdvancedEncryptionStandard;
import online.pigeonshouse.minirpc.framwork.serialize.JsonSerialize;

public class Initialization {
    public static void init() throws Exception {
        EncryptionStandardFactory.put("AES",new AdvancedEncryptionStandard("obj.dat"));
        SerializeFactory.register((byte) 1,new JsonSerialize());
        MessageFactory.register(new RemoteCallRequest());
        MessageFactory.register(new RemoteCallResponse());
    }
}

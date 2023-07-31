package online.pigeonshouse.minirpc.api.framwork.response;

import lombok.Data;
import online.pigeonshouse.minirpc.api.MiniType;
import online.pigeonshouse.minirpc.api.framwork.MessageFactory;
import online.pigeonshouse.minirpc.api.service.MiniObject;

@Data
public class AllocationServiceResponse extends MessageResponse {
    public static final MessageFactory MESSAGE_TYPE = new MessageFactory(2);

    private String uuid;
    private String sessionId;

    public AllocationServiceResponse() {
    }

    public AllocationServiceResponse(String uuid, String sessionId) {
        this.uuid = uuid;
        this.sessionId = sessionId;
    }

    public AllocationServiceResponse(Exception exception, String sessionId) {
        this.sessionId = sessionId;
        this.exception = exception;
    }

    @Override
    public Integer getType() {
        return null;
    }

    @Override
    public MiniObject getValue() {
        return new MiniObject(uuid);
    }

    @Override
    public void setValue(MiniObject value) {
        if (value.getTYPE() != MiniType.STRING) {
            throw new IllegalArgumentException("The value of AllocationServiceResponse must be a String");
        }
        this.uuid = (String) value.getValue();
    }
}

package online.pigeonshouse.minirpc.api.framwork.request;

import lombok.Data;
import online.pigeonshouse.minirpc.api.framwork.MessageFactory;
import online.pigeonshouse.minirpc.api.service.MiniObject;

@Data
public class AllocationServiceRequest extends MessageRequest {
    public static final MessageFactory MESSAGE_TYPE = new MessageFactory(53);
    private String className;
    private String sessionId;

    public AllocationServiceRequest() {
    }

    public AllocationServiceRequest(String className, String sessionId) {
        this.className = className;
        this.sessionId = sessionId;
    }

    @Override
    public Integer getType() {
        return null;
    }
}

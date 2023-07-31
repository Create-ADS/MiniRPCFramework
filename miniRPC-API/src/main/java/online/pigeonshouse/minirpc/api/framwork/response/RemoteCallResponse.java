package online.pigeonshouse.minirpc.api.framwork.response;

import lombok.Data;
import online.pigeonshouse.minirpc.api.framwork.MessageFactory;
import online.pigeonshouse.minirpc.api.service.MiniObject;

@Data
public class RemoteCallResponse extends MessageResponse {
    public static final MessageFactory MESSAGE_TYPE = new MessageFactory(1);

    private MiniObject result;
    private String sessionId;

    public RemoteCallResponse() {
    }

    public RemoteCallResponse(MiniObject result, String sessionId) {
        this.sessionId = sessionId;
        this.result = result;
    }

    public RemoteCallResponse(Throwable exception, String sessionId) {
        this.sessionId = sessionId;
        this.exception = exception;
    }

    @Override
    public Integer getType() {
        return MESSAGE_TYPE.getType();
    }

    @Override
    public MiniObject getValue() {
        return result;
    }

    @Override
    public void setValue(MiniObject value) {
        this.result = value;
    }
}
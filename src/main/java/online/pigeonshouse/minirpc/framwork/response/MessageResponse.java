package online.pigeonshouse.minirpc.framwork.response;

import lombok.Data;
import online.pigeonshouse.minirpc.framwork.Message;
import online.pigeonshouse.minirpc.framwork.MessageFactory;

@Data
public abstract class MessageResponse implements Message, SessionMessageResponse {
    public static final MessageFactory MESSAGE_TYPE = new MessageFactory(0);
    public static Integer count = 0;
    protected Throwable exception;

    public MessageResponse() {
    }
}

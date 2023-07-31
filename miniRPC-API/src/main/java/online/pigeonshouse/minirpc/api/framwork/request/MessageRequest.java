package online.pigeonshouse.minirpc.api.framwork.request;

import online.pigeonshouse.minirpc.api.framwork.Message;
import online.pigeonshouse.minirpc.api.framwork.MessageFactory;

public abstract class MessageRequest implements Message {
    public static final MessageFactory MESSAGE_TYPE = new MessageFactory(50);
    public static Integer count = 0;

    public MessageRequest() {
    }
}

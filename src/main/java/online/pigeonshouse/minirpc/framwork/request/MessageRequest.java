package online.pigeonshouse.minirpc.framwork.request;

import online.pigeonshouse.minirpc.framwork.Message;
import online.pigeonshouse.minirpc.framwork.MessageFactory;

public abstract class MessageRequest implements Message {
    public static final MessageFactory MESSAGE_TYPE = new MessageFactory(50);
    public static Integer count = 0;

    public MessageRequest() {
    }
}

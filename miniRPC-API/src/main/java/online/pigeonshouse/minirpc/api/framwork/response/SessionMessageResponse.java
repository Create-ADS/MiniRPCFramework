package online.pigeonshouse.minirpc.api.framwork.response;

import online.pigeonshouse.minirpc.api.framwork.SessionMessage;
import online.pigeonshouse.minirpc.api.service.MiniObject;

public interface SessionMessageResponse {
    MiniObject getValue();

    void setValue(MiniObject value);
}

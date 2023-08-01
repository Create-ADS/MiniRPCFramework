package online.pigeonshouse.minirpc.framwork.response;

import online.pigeonshouse.minirpc.service.MiniObject;

public interface SessionMessageResponse {
    MiniObject getValue();

    void setValue(MiniObject value);
}

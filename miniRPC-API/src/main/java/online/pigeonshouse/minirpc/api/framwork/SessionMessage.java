package online.pigeonshouse.minirpc.api.framwork;

import online.pigeonshouse.minirpc.api.service.MiniObject;

public interface SessionMessage {
    void setSessionId(String sessionId);

    String getSessionId();
}

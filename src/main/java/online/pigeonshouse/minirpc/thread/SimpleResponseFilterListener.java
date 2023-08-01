package online.pigeonshouse.minirpc.thread;

public class SimpleResponseFilterListener extends ResponseFilterListener{
    public SimpleResponseFilterListener(String sessionId) {
        super(sessionId);
    }

    public SimpleResponseFilterListener(String sessionId, Class<?>... params) {
        super(sessionId, params);
    }

    @Override
    public boolean isResponseFiltered(String sessionId) {
        return false;
    }

    @Override
    public void onResponseFiltered(Object... restParams) throws Throwable {

    }
}

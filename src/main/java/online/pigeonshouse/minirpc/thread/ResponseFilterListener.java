package online.pigeonshouse.minirpc.thread;

import cn.hutool.core.util.ClassUtil;
import lombok.Data;
import online.pigeonshouse.minirpc.service.MiniObject;

@Data
public abstract class ResponseFilterListener {
    protected String sessionId;
    protected Class<?>[] paramsType;

    public ResponseFilterListener(String sessionId) {
        this.sessionId = sessionId;
    }

    public ResponseFilterListener(String sessionId, Class<?>... params) {
        this.sessionId = sessionId;
        this.paramsType = params;
    }

    public abstract boolean isResponseFiltered(String sessionId);

    public boolean handle(Object[] restParams) throws Throwable {
        if (paramsType == null || restParams == null) {
            onResponseFiltered(null);
            return true;
        }
        if (restParams.length != paramsType.length) {
            throw new RuntimeException("restParams.length != params.length");
        }
        for (int i = 0; i < restParams.length; i++) {
            if (restParams[i] instanceof MiniObject) {
                if (ClassUtil.isAssignable(paramsType[i], MiniObject.class)) {
                    continue;
                }
                MiniObject miniObject = (MiniObject) restParams[i];
                String className = miniObject.getClassName();
                Class<?> aClass = ClassUtil.loadClass(className);
                if (!ClassUtil.isAssignable(paramsType[i], aClass)) {
                    throw new RuntimeException("Cannot cast " + className + " to " + paramsType[i].getName());
                }
                Object obj;
                try {
                    obj = aClass.newInstance();
                } catch (Exception e) {
                    try {
                        obj = aClass.getConstructor(String.class).newInstance(miniObject.getUUID());
                    }catch (Exception e1){
                        throw new RuntimeException("Cannot create instance of " + className);
                    }
                }
                restParams[i] = obj;
            }
        }
        onResponseFiltered(restParams);
        return true;
    }

    public abstract void onResponseFiltered(Object... restParams) throws Throwable;
}

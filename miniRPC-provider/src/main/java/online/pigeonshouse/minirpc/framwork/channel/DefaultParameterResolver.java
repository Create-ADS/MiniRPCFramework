package online.pigeonshouse.minirpc.framwork.channel;

import online.pigeonshouse.minirpc.api.MiniType;
import online.pigeonshouse.minirpc.api.framwork.Parameter;
import online.pigeonshouse.minirpc.api.framwork.request.RemoteCallRequest;
import online.pigeonshouse.minirpc.api.service.MiniObject;
import online.pigeonshouse.minirpc.api.util.MiniUtil;
import online.pigeonshouse.minirpc.framwork.pool.SimpleObjectPool;

import java.util.List;

public class DefaultParameterResolver implements ParameterResolver {
    @Override
    public Object[] resolve(RemoteCallRequest request, SimpleObjectPool pool) {
        List<Parameter> parameters = request.getParameters();
        MiniObject[] miniObjects = MiniUtil.toObjects(parameters.toArray(new Parameter[0]));
        Object[] objects = MiniUtil.unwrapAsList(miniObjects);
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] instanceof MiniObject) {
                MiniObject miniObject = (MiniObject) objects[i];
                if (miniObject.getTYPE() != MiniType.MINI_OBJ) {
                    continue;
                }
                Object obj = pool.get(miniObject.getClassName() + "&" + miniObject.getUUID());
                if (obj == null) {
                    throw new RuntimeException(new ClassNotFoundException(miniObject.getClassName()));
                }
                objects[i] = obj;
            }
        }
        return objects;
    }
}

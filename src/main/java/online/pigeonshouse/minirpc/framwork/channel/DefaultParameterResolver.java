package online.pigeonshouse.minirpc.framwork.channel;

import online.pigeonshouse.minirpc.MiniType;
import online.pigeonshouse.minirpc.framwork.Parameter;
import online.pigeonshouse.minirpc.framwork.request.RemoteCallRequest;
import online.pigeonshouse.minirpc.service.MiniObject;
import online.pigeonshouse.minirpc.util.MiniUtil;
import online.pigeonshouse.minirpc.framwork.pool.SimpleObjectPool;

import java.util.List;

public class DefaultParameterResolver implements ParameterResolver {
    @Override
    public Object[] resolve(RemoteCallRequest request, SimpleObjectPool pool) {
        List<Parameter> parameters = request.getParameters();
        System.out.println(parameters);
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
                    throw new RuntimeException(new ClassNotFoundException("Key:" + miniObject.getClassName() + "&" + miniObject.getUUID() + " not found in pool"));
                }
                objects[i] = obj;
            }
        }
        return objects;
    }
}

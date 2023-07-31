package online.pigeonshouse.minirpc.api.util;

import online.pigeonshouse.minirpc.api.MiniType;
import online.pigeonshouse.minirpc.api.framwork.Parameter;
import online.pigeonshouse.minirpc.api.service.MiniObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 可以改成MethodUtil那样的多类实现方式，但我不想改了
 */
public class MiniUtil {
    public static MiniObject wrap(Object param) {
        if (param instanceof Integer) {
            return new MiniObject(MiniType.INTERGER, param);
        } else if (param instanceof Double) {
            return new MiniObject(MiniType.DOUBLE, param);
        } else if (param instanceof Long) {
            return new MiniObject(MiniType.LONG, param);
        } else if (param instanceof Boolean) {
            return new MiniObject(MiniType.BOOLEAN, param);
        } else if (param instanceof String) {
            return new MiniObject(MiniType.STRING, param);
        } else if (param instanceof MiniObject) {
            return (MiniObject) param;
        } else if (param == null) {
            return MiniObject.createNull();
        } else {
            return null;
        }
    }

    public static MiniObject[] wrapAsList(Object... params) {
        List<MiniObject> miniObjects = new ArrayList<>();
        if (params != null) {
            for (Object param : params) {
                miniObjects.add(wrap(param));
            }
        }
        return miniObjects.toArray(new MiniObject[0]);
    }

    public static Object[] unwrapAsList(MiniObject... params) {
        List<Object> objects = new ArrayList<>();
        if (params != null) {
            for (MiniObject param : params) {
                objects.add(unwrap(param));
            }
        }
        return objects.toArray();
    }

    public static Object unwrap(MiniObject param) {
        switch (param.getTYPE()) {
            case INTERGER:
                return (int) ((double) param.getValue());
            case LONG:
                return (long) ((double) param.getValue());
            case DOUBLE:
            case BOOLEAN:
            case STRING:
                return param.getValue();
            case MINI_OBJ:
                return param;
            case NULL:
                return null;
            default:
                throw new RuntimeException("Unsupported type");
        }
    }

    public static Parameter[] toParameters(Object... params) {
        MiniObject[] wraps = MiniUtil.wrapAsList(params);
        if (wraps.length == 0) {
            return new Parameter[0];
        }
        Parameter[] parameters = new Parameter[wraps.length];
        for (int i = 0; i < wraps.length; i++) {
            parameters[i] = new Parameter("parameters" + i, wraps[i]);
        }
        return parameters;
    }

    public static MiniObject[] toObjects(Parameter[] parameters) {
        if (parameters.length == 0) {
            return new MiniObject[0];
        }
        MiniObject[] objects = new MiniObject[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            objects[i] = parameters[i].getValue();
        }
        return objects;
    }
}

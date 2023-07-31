package online.pigeonshouse.minirpc.api.util.method;

public class PrimitiveMatchStrategy implements ArgMatchStrategy {
    @Override
    public boolean match(Class<?> type, Object arg) {
        if (type == int.class) {
            return arg.getClass() == Integer.class;
        }else if (type == long.class) {
            return arg.getClass() == Long.class;
        }else if (type == float.class) {
            return arg.getClass() == Float.class;
        }else if (type == double.class) {
            return arg.getClass() == Double.class;
        }else if (type == boolean.class) {
            return arg.getClass() == Boolean.class;
        }else if (type == char.class) {
            return arg.getClass() == Character.class;
        }else if (type == byte.class) {
            return arg.getClass() == Byte.class;
        }else if (type == short.class) {
            return arg.getClass() == Short.class;
        }else if (arg == null && !type.isPrimitive()) {
            return true;
        }
        return type.isAssignableFrom(arg.getClass());
    }
}
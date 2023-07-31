package online.pigeonshouse.minirpc.api.util.method;

import java.lang.reflect.Method;
import java.util.stream.IntStream;

public class MethodUtil {
    public static MatchResult matchArgType(Class<?> type, Object arg, ArgMatchStrategy strategy) {
        if (!strategy.match(type, arg)) {
            return MatchResult.ARG_TYPE_NOT_MATCH;
        }
        return MatchResult.SUCCESS;
    }

    public static MatchResult checkVarArgsMatch(Class<?> varArgType, Object[] params) {
        for (Object arg : params) {
            if (!varArgType.isAssignableFrom(arg.getClass())) {
                return MatchResult.ARG_TYPE_NOT_MATCH;
            }
        }
        return MatchResult.SUCCESS;
    }

    public static MatchResult isMatch(Method method, Object[] params) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length > 0 && parameterTypes[parameterTypes.length - 1].isArray()) {
            return checkVarArgsMatch(parameterTypes[parameterTypes.length - 1].getComponentType(), params);
        }
        if (parameterTypes.length != params.length) {
            return MatchResult.ARG_COUNT_NOT_MATCH;
        }
        ArgMatchStrategy strategy = new PrimitiveMatchStrategy();
        return IntStream.range(0, parameterTypes.length)
                .mapToObj(i -> matchArgType(parameterTypes[i], params[i], strategy))
                .anyMatch(r -> r != MatchResult.SUCCESS) ? MatchResult.ARG_TYPE_NOT_MATCH : MatchResult.SUCCESS;
    }
}
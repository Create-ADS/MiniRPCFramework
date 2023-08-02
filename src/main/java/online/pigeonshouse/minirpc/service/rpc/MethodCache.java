package online.pigeonshouse.minirpc.service.rpc;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.map.TableMap;
import cn.hutool.core.util.StrUtil;
import online.pigeonshouse.minirpc.MiniService;
import online.pigeonshouse.minirpc.service.PublicService;
import online.pigeonshouse.minirpc.util.method.MatchResult;
import online.pigeonshouse.minirpc.util.method.MethodUtil;

import java.lang.reflect.Method;
import java.util.List;

public class MethodCache {

    private final TableMap<String, Method> cache = new TableMap<>();

    public void registerClass(Class<? extends Service> clazz) {
        if (!AnnotationUtil.hasAnnotation(clazz, MiniService.class)) {
            throw new RuntimeException(String.format("Your class[%s] must be annotated by @MiniService", clazz));
        }
        MiniService miniService = AnnotationUtil.getAnnotation(clazz, MiniService.class);
        if (StrUtil.hasBlank(miniService.name())) {
            throw new RuntimeException(String.format("Service name cannot be blank, as class[%s]", clazz));
        }
        for (Method method : clazz.getMethods()) {
            if (!AnnotationUtil.hasAnnotation(method, PublicService.class)) {
                continue;
            }
            String methodName = method.getName();
            cache.put(methodName, method);
        }
    }

    public Method getMethod(String key, Object[] obj) {
        List<Method> values = cache.getValues(key);
        if (values.size() == 0) {
            return null;
        }
        for (Method method : values) {
            if (MethodUtil.isMatch(method, obj) == MatchResult.SUCCESS) {
                return method;
            }
        }
        return null;
    }

}
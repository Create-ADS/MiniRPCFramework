package online.pigeonshouse.minirpc.service.rpc;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.StrUtil;
import online.pigeonshouse.minirpc.MiniService;
import online.pigeonshouse.minirpc.service.PublicService;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class MethodCache {

    private final Map<String, Method> cache = new HashMap<>();

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
            boolean aStatic = Modifier.isStatic(method.getModifiers());
            StringBuilder paramNamesString = new StringBuilder();
            for (int i = 0; i < method.getParameterCount(); i++) {
                paramNamesString.append("param").append(i).append(",");
            }
            String key = clazz.getName() + "&" + methodName + "&" + (aStatic ? "static" : "non-static") + "&" + paramNamesString;
            cache.put(key, method);
        }
    }

    public Method getMethod(String key) {
        return cache.get(key);
    }

}
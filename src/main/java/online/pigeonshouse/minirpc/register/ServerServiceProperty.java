package online.pigeonshouse.minirpc.register;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.map.TableMap;
import online.pigeonshouse.minirpc.service.PublicService;
import online.pigeonshouse.minirpc.util.method.MethodUtil;
import online.pigeonshouse.minirpc.service.ServerService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

public class ServerServiceProperty {
    private final ServerService service;
    private final TableMap<String, Method> methods = new TableMap<>();
    private final TableMap<String, Method> staticMethods = new TableMap<>();

    public ServerServiceProperty(ServerService service) {
        this.service = service;
        for (Method method : service.getClass().getMethods()) {
            if (AnnotationUtil.hasAnnotation(method, PublicService.class)) {
                if (Modifier.isStatic(method.getModifiers())) {
                    staticMethods.put(method.getName(), method);
                } else {
                    methods.put(method.getName(), method);
                }
            }
        }
    }

    public Object invokeStaticMethod(String methodName, Object[] params) throws Exception {
        List<Method> values = staticMethods.getValues(methodName);
        if (values.size() == 0) {
            throw new NoSuchMethodException("No such method: " + methodName);
        }
        for (Method method : values) {
            switch (MethodUtil.isMatch(method, params)) {
                case SUCCESS:
                    return method.invoke(null, params);
                case ARG_COUNT_NOT_MATCH:
                    throw new NoSuchMethodException("Arg count not match: " + methodName);
                case ARG_TYPE_NOT_MATCH:
                    throw new NoSuchMethodException("Arg type not match: " + methodName);
            }
        }
        throw new NoSuchMethodException("No such method: " + methodName);
    }

    public Object invokeMethod(String methodName, Object[] params) throws Throwable {
        List<Method> methods = this.methods.getValues(methodName);
        if (methods.isEmpty()) {
            throw new NoSuchMethodException("No such method: " + methodName);
        }
        for (Method method : methods) {
            switch (MethodUtil.isMatch(method, params)) {
                case SUCCESS:
                    try {
                        return method.invoke(service, params);
                    } catch (InvocationTargetException e) {
                        throw e.getTargetException();
                    }
            }
        }
        throw new NoSuchMethodException("No match found for method: " + methodName);
    }
}

package online.pigeonshouse.minirpc.register;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import online.pigeonshouse.minirpc.api.MiniService;
import online.pigeonshouse.minirpc.api.exception.InitializationException;
import online.pigeonshouse.minirpc.api.service.rpc.Service;
import online.pigeonshouse.minirpc.service.ServerService;

/**
 * 此类用于扫描指定包下的所有类，将标注了@MiniService注解的类注册到注册中心
 * 内部提供一个将指定类注册到注册中心的方法
 * 注册中心的类名：ServiceRegister
 */
public class ServiceRegisterFactory {
    static {
        referService("online.pigeonshouse.minirpc.service.impl");
    }

    /**
     * 注册指定包下的所有符合条件的类到注册中心
     */
    public static void referService(String packageName)  {
        ClassUtil.scanPackage(packageName).forEach(clazz -> {
            try {
                referService(clazz);
            } catch (IllegalAccessException | InstantiationException | InitializationException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 注册指定类到注册中心
     */
    public static void referService(Class<?> clazz) throws IllegalAccessException, InstantiationException, InitializationException {
        if (!isValidService(clazz)) {
            new InitializationException("Class " + clazz.getName() + " is not a valid service").printStackTrace();
            return;
        }
        MiniService miniService = AnnotationUtil.getAnnotation(clazz, MiniService.class);
        String serviceName = miniService.name();
        String serviceVersion = miniService.version();
        String serviceGroup = miniService.group();
        if (StrUtil.hasBlank(serviceName)) {
            throw new RuntimeException("Class " + clazz.getName() + " annotated by @MiniService has no name");
        }
        ServerService serviceObject = (ServerService) clazz.newInstance();
        ServiceRegisterManage.getInstance().register(serviceGroup, serviceName, serviceVersion, serviceObject);
    }

    /**
     * 判断服务是否有效
     */
    public static boolean isValidService(Class<?> clazz) {
        if (!AnnotationUtil.hasAnnotation(clazz, MiniService.class)) {
            return false;
        }
        if (!ClassUtil.isAssignable(ServerService.class, clazz)) {
            return false;
        }
        return true;
    }
}

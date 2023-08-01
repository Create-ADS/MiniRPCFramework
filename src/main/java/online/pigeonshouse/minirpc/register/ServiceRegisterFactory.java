package online.pigeonshouse.minirpc.register;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import io.netty.channel.Channel;
import online.pigeonshouse.minirpc.MiniService;
import online.pigeonshouse.minirpc.exception.InitializationException;
import online.pigeonshouse.minirpc.framwork.channel.ProtocolSelectorHandler;
import online.pigeonshouse.minirpc.framwork.pool.SimpleObjectPool;
import online.pigeonshouse.minirpc.framwork.pool.UserSessionPoolManager;
import online.pigeonshouse.minirpc.service.ServerService;

import java.lang.reflect.InvocationTargetException;

/**
 * 此类用于扫描指定包下的所有类，将标注了@MiniService注解的类注册到注册中心
 * 内部提供一个将指定类注册到注册中心的方法
 * 注册中心的类名：ServiceRegister
 */
public class ServiceRegisterFactory {
    public static ServiceRegisterManage referService(String packageName, Channel channel, ServiceRegisterManage manage)  {
        ClassUtil.scanPackage(packageName).forEach(clazz -> {
            System.out.println("Scanning class: " + clazz.getName());
            try {
                referService(clazz, channel, manage);
            } catch (IllegalAccessException | InstantiationException | NoSuchMethodException |
                     InvocationTargetException e) {
                e.printStackTrace();
            }
        });
        return manage;
    }

    /**
     * 注册指定类到注册中心
     */
    public static ServiceRegisterManage referService(Class<?> clazz, Channel channel, ServiceRegisterManage manage) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        if (!isValidService(clazz)) {
            new InitializationException("Class " + clazz.getName() + " is not a valid service").printStackTrace();
            return null;
        }
        MiniService miniService = AnnotationUtil.getAnnotation(clazz, MiniService.class);
        String serviceName = miniService.name();
        String serviceVersion = miniService.version();
        String serviceGroup = miniService.group();
        if (StrUtil.hasBlank(serviceName)) {
            throw new RuntimeException("Class " + clazz.getName() + " annotated by @MiniService has no name");
        }
        UserSessionPoolManager poolManager = ProtocolSelectorHandler.getUserSessionPoolManager();
        SimpleObjectPool put = poolManager.get(channel);
        ServerService serviceObject = (ServerService) clazz.getConstructor(SimpleObjectPool.class, Channel.class)
                .newInstance(put, channel);
        manage.register(serviceGroup, serviceName, serviceVersion, serviceObject);
        return manage;
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

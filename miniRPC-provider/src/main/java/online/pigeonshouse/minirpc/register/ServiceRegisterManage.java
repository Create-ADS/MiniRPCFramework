package online.pigeonshouse.minirpc.register;

import online.pigeonshouse.minirpc.service.ServerService;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册中心，用于注册服务
 */
public class ServiceRegisterManage {
    private static ServiceRegisterManage serviceRegisterManage = new ServiceRegisterManage();
    private Map<String, ServerServiceProperty> serviceObjectMap = new HashMap<>();

    public static ServiceRegisterManage getInstance() {
        return serviceRegisterManage;
    }

    private ServiceRegisterManage() {
    }

    /**
     * 注册服务
     *
     * @param version 服务版本
     * @param group   服务分组
     * @param name    服务名
     * @param service 服务对象
     */
    public void register(String group, String name, String version, ServerService service) {
        String key = group + name + version;
        serviceObjectMap.put(key, new ServerServiceProperty(service));
    }

    // 获取服务
    public ServerServiceProperty lookup(String group, String name, String version) {
        String key = group + name + version;
        return serviceObjectMap.get(key);
    }
}

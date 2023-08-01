package online.pigeonshouse.minirpc.register;

import io.netty.channel.Channel;
import online.pigeonshouse.minirpc.service.ServerService;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册中心，用于注册服务
 */
public class ServiceRegisterManage {
    private Map<String, ServerServiceProperty> serviceObjectMap = new HashMap<>();
    private Channel channel;

    public ServiceRegisterManage(Channel channel) {
        this.channel = channel;
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

    public void scanServicePackage(String packageName) {
        ServiceRegisterFactory.referService(packageName, channel,this);
    }
}

package online.pigeonshouse.minirpc;

import java.lang.annotation.*;


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface MiniService {
    /**
     * 服务版本号
     */
    String version();

    /**
     * 服务分组
     * 意思是一个接口有多个实现类，可以用group来区分，代码举例：
     */
    String group() default "";

    /**
     * 服务名称
     */
    String name() default "";

    /**
     * 服务是否开启监控
     * 用于监控服务的调用次数、调用时间等
     */
    boolean monitor() default false;
}

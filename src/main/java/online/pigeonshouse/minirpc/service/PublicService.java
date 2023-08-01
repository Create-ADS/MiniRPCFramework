package online.pigeonshouse.minirpc.service;

/**
 * 此注解标记与方法上, 将此方法暴露为远程服务
 */

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PublicService {
    boolean isWait() default false;
}
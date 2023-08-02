package online.pigeonshouse.minirpc.service;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PublicService {
    boolean isWait() default false;
    long timeout() default 2000;
}

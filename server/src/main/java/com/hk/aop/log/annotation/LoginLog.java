package com.hk.aop.log.annotation;

import java.lang.annotation.*;

/**
 * @author huangkun
 * @date 2025/7/8 9:42
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginLog {
    String value() default "";
    String desc() default "";
}

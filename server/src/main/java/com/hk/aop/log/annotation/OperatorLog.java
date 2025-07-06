package com.hk.aop.log.annotation;

import java.lang.annotation.*;

/**
 * @author huangkun
 * @date 2025/7/6 10:50
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperatorLog {

    String value() default "";
    String desc() default "";
    String type() default "info";


}

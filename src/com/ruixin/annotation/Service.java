package com.ruixin.annotation;

import java.lang.annotation.*;

/**
 * Created by ruixin on 2018/7/28.
 * Description:service层注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service{

    String value() default "";
}

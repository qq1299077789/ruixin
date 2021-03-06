package com.ruixin.annotation;

import java.lang.annotation.*;

/**
 * Created by ruixin on 2018/7/28.
 * Description: get请求处理注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GetMapping {

    String[] value() default "";
}

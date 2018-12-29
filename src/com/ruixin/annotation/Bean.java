package com.ruixin.annotation;

import java.lang.annotation.*;

/**
 * Created by ruixin on 2018/7/28.
 * Description:bean注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {

     String value() default "";
     //初始化后加载的方法
     String[] init() default "";
}

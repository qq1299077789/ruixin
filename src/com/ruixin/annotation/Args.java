package com.ruixin.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by ruixin on 2018/7/28.
 * Description: 参数获取注解
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Args {
	
	String value() default "";
	String defaultValue() default "";
	boolean require() default false;
}

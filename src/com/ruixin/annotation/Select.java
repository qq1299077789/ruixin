package com.ruixin.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @Author:ruixin
 * @Date: 2018年12月11日 下午4:19:46
 * @Description:查询
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Select {

	String value() default "";
	String sql() default "";
	Class<?> returnType();
}

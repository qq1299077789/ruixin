package com.ruixin.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author:ruixin
 * @Date: 2018年12月27日 上午9:19:20
 * @Description:查询
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SelectKey {

	//需要执行的SQL
	String statement();
	//返回类型
	Class<?> resultType();
	//执行顺序
	Order order();
	//映射field
	String keyProperty();
}

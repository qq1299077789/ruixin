package com.ruixin.annotation;

import java.lang.annotation.*;

/**
 * @Author:ruixin
 * @Date: 2018年12月25日 上午11:08:48
 * @Description:方法执行前拦截
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Before {

   //监听器的类
   Class<?> clz();
   //需要执行的方法
   String[] method();
}

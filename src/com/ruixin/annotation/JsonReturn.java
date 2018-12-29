package com.ruixin.annotation;

import java.lang.annotation.*;

/**
 * Created by ruixin on 2018/7/28.
 * Description:返回json参数
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonReturn {
}

package com.ruixin.proxy;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodProxy;
/**
 * @Author:ruixin
 * @Date: 2018年12月14日 上午9:18:56
 * @Description:代理处理
 */
public interface ProxyHandler {

	public Object handler(Object obj,Method method,Object[] args,MethodProxy proxy);
	
}

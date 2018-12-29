package com.ruixin.proxy;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
/**
 * @Author:ruixin
 * @Date: 2018年12月11日 下午12:42:31
 * @Description:Service动态代理
 */
public class ServiceProxy implements MethodInterceptor{

	public static Object newInstance(Class<?> clz) {  
		Enhancer enhancer = new Enhancer();  
		enhancer.setSuperclass(clz);  
		enhancer.setCallback(new ServiceProxy());  
        return clz.cast(enhancer.create());
    }

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy arg3) throws Throwable {
		return new ServiceProxyHandler().handler(obj,method,args,arg3);
	}
	
}

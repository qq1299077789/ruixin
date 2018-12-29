package com.ruixin.proxy;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
/**
 * @Author:ruixin
 * @Date: 2018年12月11日 下午12:42:31
 * @Description:dao动态代理
 */
public class DaoProxy implements MethodInterceptor{
	
	public static Object newInstance(Class<?> interfaces) {  
		Enhancer enhancer = new Enhancer();  
		enhancer.setSuperclass(interfaces);  
		enhancer.setCallback(new DaoProxy());  
        return interfaces.cast(enhancer.create());
    }

	@Override
	public Object intercept(Object arg0, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		return new DaoProxyHandler().handler(arg0,method,args,proxy);
	}
	
}

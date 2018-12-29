package com.ruixin.proxy;

import java.lang.reflect.Method;
import java.util.List;

import com.ruixin.config.HandleException;
import com.ruixin.simpleDB.Mapper;
import com.ruixin.simpleDB.MapperContainer;
import com.ruixin.simpleDB.MapperHandler;
import com.ruixin.simpleDB.ParamContainer;
import com.ruixin.simpleDB.Parameter;
import com.ruixin.simpleDB.Provider;

import net.sf.cglib.proxy.MethodProxy;

/**
 * @Author:ruixin
 * @Date: 2018年12月11日 下午5:07:24
 * @Description:处理dao层代理方法
 */
public class DaoProxyHandler implements ProxyHandler{

	/**
	 * @param method
	 * @param args
	 * @return
	 * @Description:处理代理方法
	 */
	@Override
	public Object handler(Object obj,Method method,Object[] args,MethodProxy proxy){
		List<Parameter> params = ParamContainer.getInstance().getParams(method.getDeclaringClass().getSimpleName(), method.getName());
		Mapper mapper = MapperContainer.getInstance().getMapper(method.getDeclaringClass().getSimpleName()+"."+method.getName());
		Provider provider = MapperContainer.getInstance().getProvider(method.getDeclaringClass().getSimpleName()+"."+method.getName());
		MapperHandler mapperHandler=new MapperHandler(method,args,params);
		Object result=null;
		if(mapper==null&&provider==null){
			HandleException.getInstance().handler(proxy.getSuperName()+"."+method.getName()+" 缺少Mapper", new Exception());
		}else if(mapper!=null&&provider!=null){
			HandleException.getInstance().handler(proxy.getSuperName()+"."+method.getName()+" Mapper和Provider不能同时使用", new Exception());
		}else if(mapper!=null){
			result=mapperHandler.handleMapper(mapper);
		}else{
			result=mapperHandler.handleProvider(provider);			
		}
		return result;
	}
}

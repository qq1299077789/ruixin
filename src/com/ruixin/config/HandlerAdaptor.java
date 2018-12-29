package com.ruixin.config;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author:ruixin
 * @Date: 2018年11月13日 下午12:33:19
 * @Description:分发http请求
 */
public class HandlerAdaptor {

	private HttpServletRequest request;
	private HttpServletResponse response;
	private HandlerMethod handlerMethod;
	
	public HandlerAdaptor(HttpServletRequest request,HttpServletResponse response) {
		this.request=request;
		this.response=response;
	}
	
	/**
	 * @Description:处理handler请求
	 */
	public Object handle(){
		String httpUri=request.getRequestURI();
		String projectUri=request.getServletContext().getContextPath();
		httpUri=httpUri.substring(projectUri.length(), httpUri.length());
		String httpType=request.getMethod();
		Map<String, Object> mapping = HandlerMapping.instance.getMapping(httpType, httpUri);
		if(mapping==null||mapping.isEmpty()){
			//404异常
			HandleException.getInstance().handlerView(response, HandleException.NOTFOUND);
			return null;
		}else{
			Object web=mapping.get(HandlerMapping.instance.Key_Web);
			Method method=(Method) mapping.get(HandlerMapping.instance.Key_Method);
			handlerMethod=new HandlerMethod(request, response, web, method);
			return handlerMethod.invok();
		}
	}
	
	/**
	 * @return
	 * @Description:返回类型是否是json格式
	 */
	public boolean returnJson(){
		return handlerMethod.isJsonData();
	}
	
	/**
	 * @return
	 * @Description:返回类型是否是ModelMap
	 */
	public boolean returnModelMap(){
		return handlerMethod.returnModelMap();
	}
}

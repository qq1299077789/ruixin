package com.ruixin.config;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ruixin.annotation.JsonReturn;
import com.ruixin.upload.MultipartFile;
import com.ruixin.util.AnnoUtil;
import com.ruixin.util.ReflectUtil;
/**
 * @Author:ruixin
 * @Date: 2018年11月13日 下午2:56:21
 * @Description:处理handler方法映射
 */
public class HandlerMethod {

	private Method method; //handler的method
	private Object bean;  //controller
	private Object[] parameters; //处理后的参数
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Map<String,String[]> args;//前台传过来的参数
	private boolean isJsonData=false;//是否是JSON数据
	
	public HandlerMethod(HttpServletRequest request,HttpServletResponse response,Object bean,Method method) {
		this.request=request;
		this.response=response;
		this.method=method;
		this.bean=bean;	
		this.args=request.getParameterMap();
		this.isJsonData=AnnoUtil.instance.hasAnnotations(method,JsonReturn.class);
	}
	
	/**
	 * @Description:调用handlerMathod
	 */
	public Object invok(){
		Object[] args=getParameter();
		try {
			return ReflectUtil.instance.callMethod(bean, method, args);
		} catch (Throwable e) {
			HandleException.getInstance().handlerView(response, HandleException.NOTFOUND);
		}
		return null;
	}
	
	/**
	 * @param method
	 * @return
	 * @Description:转换参数
	 */
	private Object[] getParameter(){
		Class<?>[] parameterTypes = method.getParameterTypes();
		this.parameters = new Object[parameterTypes.length];
		for(int i=0;i<parameterTypes.length;i++){
			Class<?> parameterType=parameterTypes[i];
			if(parameterType==HttpServletRequest.class){
				parameters[i]=request;
			}else if(parameterType==HttpServletResponse.class){
				parameters[i]=response;
			}else if(parameterType==HttpSession.class){
				parameters[i]=request.getSession();
			}else if(parameterType==ModelMap.class){
				parameters[i]=new ModelMap(request);
			}else if(parameterType==MultipartFile.class){
				parameters[i]=new MultipartFile(request);
			}
			//设置其他的接收参数
		}
		AnnotationConfig.instance.parseArgs(method, parameters, args);
		return parameters;
	}
	
	/**
	 * @return
	 * @Description:是否含有JsonReturn注解
	 */
	public boolean isJsonData(){
		return isJsonData;
	}
	
	/**
	 * @return
	 * @Description:返回类型是否是ModelMap
	 */
	public boolean returnModelMap(){
		return method.getReturnType()==ModelMap.class;
	}
}

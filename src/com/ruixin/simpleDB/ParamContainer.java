package com.ruixin.simpleDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Author:ruixin
 * @Date: 2018年12月12日 上午10:16:11
 * @Description:存储dao方法参数的Param
 */
public class ParamContainer {
	//类名，方法名，参数
	private Map<String,HashMap<String,List<Parameter>>> params=new HashMap<>();
	private static ParamContainer instance=null;
	
	private ParamContainer() {
	}
	
	/**
	 * @param parameter
	 * @Description:添加Param
	 */
	public void addParam(Parameter parameter){
		if(!params.containsKey(parameter.getClassName())){
			params.put(parameter.getClassName(),new HashMap<String,List<Parameter>>());
		}
		if(!params.get(parameter.getClassName()).containsKey(parameter.getMethodName())){
			params.get(parameter.getClassName()).put(parameter.getMethodName(), new ArrayList<>());
		}
		params.get(parameter.getClassName()).get(parameter.getMethodName()).add(parameter.getIndex(), parameter);
	}
	
	/**
	 * @param className
	 * @param methodName
	 * @return
	 * @Description:获取方法里面的Params
	 */
	public List<Parameter> getParams(String className,String methodName){
		if(params.containsKey(className)){
			return params.get(className).get(methodName);
		}
		return null;
	}
	
	public static ParamContainer getInstance(){
		if(instance==null){
			synchronized (ParamContainer.class) {
				if(instance==null){
					instance=new ParamContainer();
				}
			}
		}
		return instance;
	}
}

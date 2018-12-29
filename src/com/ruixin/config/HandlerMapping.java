package com.ruixin.config;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @Author:ruixin
 * @Date: 2018年11月12日 下午8:13:42
 * @Description:Mapping管理类
 */
public class HandlerMapping {

	private HandlerMapping(){}
	public static HandlerMapping instance = new HandlerMapping();
	private Map<Object,Map<String,Method>> getMapping=new HashMap<Object,Map<String,Method>>();
	private Map<Object,Map<String,Method>> postMapping=new HashMap<Object,Map<String,Method>>();
	private Map<Object,Map<String,Method>> putMapping=new HashMap<Object,Map<String,Method>>();
	private Map<Object,Map<String,Method>> deleteMapping=new HashMap<Object,Map<String,Method>>();
	private Map<Object,Map<String,Method>> allMapping=new HashMap<Object,Map<String,Method>>();
	private String httpTypeGet="GET";
	private String httpTypePost="POST";
	private String httpTypePut="PUT";
	private String httpTypeDelete="DELETE";
	private String httpTypeAll="ALL";
	public String Key_Web="Web";
	public String Key_Method="Method";
	
	/**
	 * @param web
	 * @param url url
	 * @param method 方法
	 * @Description:添加一个mapping
	 */
	public void addMapping(Object web,String httpType,String url,Method method){
		Map<Object,Map<String,Method>> mapping=getMappingContainer(httpType);
		if(!mapping.containsKey(web)){
			mapping.put(web, new HashMap<String,Method>());
		}
		//Mapping查重
		if(mapping.get(web).containsKey(url)){
			HandleException.getInstance().handler(url+"Mapping重复异常",new Exception());
		}else{
			mapping.get(web).put(url, method);			
		}
	}
	
	/**
	 * @param httpType
	 * @return
	 * @Description:通过请求类型获取Mapping容器
	 */
	public Map<Object,Map<String,Method>> getMappingContainer(String httpType){
		if(httpTypeGet.equalsIgnoreCase(httpType)){
			return getMapping;
		}else if(httpTypePost.equalsIgnoreCase(httpType)){
			return postMapping;
		}else if(httpTypePut.equalsIgnoreCase(httpType)){
			return putMapping;
		}else if(httpTypeDelete.equalsIgnoreCase(httpType)){
			return deleteMapping;
		}else{
			return allMapping;
		}
	}
	
	/**
	 * @param httpType
	 * @param uri
	 * @return
	 * @Description:通过请求类型和
	 */
	public Map<String,Object> getMapping(String httpType,String uri){
		Map<Object, Map<String, Method>> container = getMappingContainer(httpType);
		Map<String,Map<String,Object>> results=new HashMap<>();
		Map<String,Object> result=null;
		//首先通过请求类型搜索
		for(Entry<Object,Map<String,Method>> entry:container.entrySet()){
			Object web=entry.getKey();
			Map<String,Method> map=entry.getValue();
			for(Entry<String, Method> entry1:map.entrySet()){
				String url=entry1.getKey();
				Method method=entry1.getValue();
				//url优先级映射
				if(uri.startsWith(url)){
					result=new HashMap<>();
					result.put(Key_Web, web);
					result.put(Key_Method,method);
					results.put(url, result);
				}
			}
		}
		//请求类型找不到，就在All类型中查找
		Map<Object, Map<String, Method>> container1 = getMappingContainer(httpTypeAll);
		for(Entry<Object,Map<String,Method>> entry:container1.entrySet()){
			Object web=entry.getKey();
			Map<String,Method> map=entry.getValue();
			for(Entry<String, Method> entry1:map.entrySet()){
				String url=entry1.getKey();
				Method method=entry1.getValue();
				//url优先级映射
				if(uri.startsWith(url)){
					result=new HashMap<>();
					result.put(Key_Web, web);
					result.put(Key_Method,method);
					results.put(url, result);
				}
			}
		}
		return getMapping(results);
	}
	
	/**
	 * @param results
	 * @return
	 * @Description:获取最优匹配
	 */
	public Map<String,Object> getMapping(Map<String,Map<String,Object>> results){
		String uri="";
		for(Entry<String, Map<String,Object>> entry:results.entrySet()){
			if(entry.getKey().length()>uri.length()){
				uri=entry.getKey();
			}
		}
		return results.get(uri);
	}
}

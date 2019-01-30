package com.ruixin.config;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ruixin.util.UrlUtil;

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
	public String httpTypeGet="GET";
	public String httpTypePost="POST";
	public String httpTypePut="PUT";
	public String httpTypeDelete="DELETE";
	public String httpTypeAll="ALL";
	public String Key_Web="Web";
	public String Key_Method="Method";
	public String Key_Url="Url";
	
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
	 * @Description:通过请求类型和请求链接查询mapping
	 */
	public Map<String,Object> getMapping(String httpType,String uri){
		Map<Object, Map<String, Method>> container = getMappingContainer(httpType);
		Map<String,Integer> urlPattern=new HashMap<>();
		Map<String,Map<String,Object>> results=new HashMap<>();
		Map<String,Object> result=null;
		int maxUrlPatternIndex=-1;//最大精准匹配系数
		
		//首先通过请求类型搜索
		for(Entry<Object,Map<String,Method>> entry:container.entrySet()){
			Object web=entry.getKey();
			Map<String,Method> map=entry.getValue();
			for(Entry<String, Method> entry1:map.entrySet()){
				String url=entry1.getKey();
				Method method=entry1.getValue();
				//url优先级映射
				int urlPatternIndex = UrlUtil.urlPattern(uri, url);
				if(urlPatternIndex==UrlUtil.EQ){ //精准匹配直接返回
					result=new HashMap<>();
					result.put(Key_Web, web);
					result.put(Key_Method,method);
					result.put(Key_Url, url);
					return result;
				}else if(urlPatternIndex!=-1&&urlPatternIndex>maxUrlPatternIndex){
					maxUrlPatternIndex=urlPatternIndex;
					urlPattern.put(url,urlPatternIndex);
					result=new HashMap<>();
					result.put(Key_Web, web);
					result.put(Key_Method, method);
					result.put(Key_Url, url);
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
				int urlPatternIndex = UrlUtil.urlPattern(uri, url);
				if(urlPatternIndex==UrlUtil.EQ){ //精准匹配直接返回
					result=new HashMap<>();
					result.put(Key_Web, web);
					result.put(Key_Method,method);
					result.put(Key_Url, url);
					return result;
				}else if(urlPatternIndex!=-1&&urlPatternIndex>maxUrlPatternIndex){
					maxUrlPatternIndex=urlPatternIndex;
					urlPattern.put(url,urlPatternIndex);
					result=new HashMap<>();
					result.put(Key_Web, web);
					result.put(Key_Method, method);
					result.put(Key_Url, url);
					results.put(url, result);
				}
			}
		}
		String baseUrl=getMapping(urlPattern,maxUrlPatternIndex);
		return results.get(baseUrl);
	}
	
	/**
	 * @param results
	 * @return
	 * @Description:获取最优匹配
	 */
	public String getMapping(Map<String,Integer> results,int maxUrlPatternIndex){
		Map<String,Integer> map = new HashMap<>();
		List<String> urls=new ArrayList<>(); //防止出现多个匹配系数一致的情况
		for(Entry<String,Integer> entry:results.entrySet()){
			String url=entry.getKey();
			Integer urlPattern=entry.getValue();
			if(urlPattern==maxUrlPatternIndex){
				urls.add(url);
			}
		}
		if(urls.size()==1){
			return urls.get(0);
		}else if(urls.size()>1){ //多个匹配系数一致
			
			return null;
		}else{
			return null;
		}
	}
	
}

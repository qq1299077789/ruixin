package com.ruixin.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
/**
 * @Author:ruixin
 * @Date: 2018年11月13日 下午2:27:43
 * @Description:bean工厂
 */
public class BeanFactory {

	public static BeanFactory instance=new BeanFactory();
	private Map<String,Object> factory=new HashMap<String,Object>();
	private Map<String,Class<?>> beans=new HashMap<String,Class<?>>();
	private Map<String,Class<?>> webs=new HashMap<String,Class<?>>();
	private Map<String,Class<?>> services=new HashMap<String,Class<?>>();
	private Map<String,Class<?>> daos=new HashMap<String,Class<?>>();
	
	private BeanFactory(){}
	
	/**
	 * @param key
	 * @param obj
	 * @Description:添加bean
	 */
	public void addBean(String key,Class<?> obj){
		beans.put(key, obj);
	}
	
	/**
	 * @param key
	 * @return
	 * @Description:获取bean
	 */
	public Class<?> getBean(String key){
		return beans.get(key);
	}
	
	/**
	 * @return
	 * @Description:获取所有bean
	 */
	public Map<String,Class<?>> getBeans(){
		return beans;
	}
	
	/**
	 * @return
	 * @Description:获取bean数目
	 */
	public int getBeanSize(){
		return beans.size();
	} 
	
	/**
	 * @param key
	 * @param obj
	 * @Description:添加service
	 */
	public void addService(String key,Class<?> obj){
		services.put(key, obj);
	}
	
	/**
	 * @param key
	 * @return
	 * @Description:获取service
	 */
	public Class<?> getService(String key){
		return services.get(key);
	}
	
	/**
	 * @param clz
	 * @return
	 * @Description:通过类查找beanId
	 */
	public String getServiceIdByClass(Class<?> clz){
		for(Entry<String, Class<?>> entry:services.entrySet()){
			if(clz.getSimpleName().equals(entry.getValue().getSimpleName())){
				return entry.getKey();
			}
		}
		return "";
	}
	
	/**
	 * @return
	 * @Description:获取所有service
	 */
	public Map<String,Class<?>> getServices(){
		return services;
	}
	
	/**
	 * @return
	 * @Description:获取service数目
	 */
	public int getServiceSize(){
		return services.size();
	} 
	
	/**
	 * @param key
	 * @param obj
	 * @Description:添加一个web
	 */
	public void addWeb(String key,Class<?> obj){
		webs.put(key, obj);
	}
	
	/**
	 * @param key
	 * @return
	 * @Description:获取web
	 */
	public Class<?> getWeb(String key){
		return webs.get(key);
	}
	
	/**
	 * @return
	 * @Description:获取所有web
	 */
	public Map<String,Class<?>> getWebs(){
		return webs;
	}
	
	/**
	 * @return
	 * @Description:获取web个数
	 */
	public int getWebSize(){
		return webs.size();
	}
	
	/**
	 * @param key
	 * @param clz
	 * @Description:添加一个dao
	 */
	public void addDao(String key,Class<?> clz){
		daos.put(key, clz);
	}
	
	/**
	 * @param key
	 * @return
	 * @Description:获取一个dao
	 */
	public Class<?> getDao(String key){
		return daos.get(key);
	}
	
	/**
	 * @return
	 * @Description:获取dao的数据
	 */
	public int getDaoSize(){
		return daos.size();
	}
	
	/**
	 * @return
	 * @Description:获取所有dao
	 */
	public Map<String,Class<?>> getDaos(){
		return daos;
	}
	
	/**
	 * @param key
	 * @param obj
	 * @Description:添加一个bean对象
	 */
	public void addBeanObj(String key,Object obj){
		if(factory.containsKey(key)){
			throw new RuntimeException("bean "+key+" 已存在异常");
		}
		factory.put(key, obj);
	}
	
	/**
	 * @param key
	 * @return
	 * @Description:获取一个bean对象
	 */
	public Object getBeanObj(String key){
		return factory.get(key);
	}
	
	/**
	 * @return
	 * @Description:获取bean对象的个数
	 */
	public int size(){
		return factory.size();
	}
}

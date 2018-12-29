package com.ruixin.simpleDB;

import java.util.HashMap;

import com.ruixin.config.HandleException;

/**
 * @Author:ruixin
 * @Date: 2018年12月11日 下午5:13:14
 * @Description:Mapper操作容器
 */
public class MapperContainer {

	//存储 mapperID,Mapper
	private HashMap<String,Mapper> mappers=new HashMap<String,Mapper>();
	
	//存储 providerID,provider
	private HashMap<String,Provider> providers=new HashMap<String,Provider>();
	
	private static MapperContainer instance=null;
	
	private MapperContainer(){}
	
	/**
	 * @param mapper
	 * @Description:添加Mapper
	 */
	public void addMapper(Mapper mapper){
		if(mappers.containsKey(mapper.getId())){
			HandleException.getInstance().handler("mapper "+mapper.getId()+" 重复异常",new Exception());
		}else{
			mappers.put(mapper.getId(),mapper);
		}
	}
	
	/**
	 * @param key
	 * @return
	 * @Description:通过mapperKey获取Mapper
	 */
	public Mapper getMapper(String key){
		return mappers.get(key);
	}
	
	/**
	 * @return
	 * @Description:Mapper数量
	 */
	public int getMapperSize(){
		return mappers.size();
	}

	/**
	 * @param provider
	 * @Description:添加动态查询Provider
	 */
	public void addProvider(Provider provider){
		if(providers.containsKey(provider.getId())){
			HandleException.getInstance().handler("provider "+provider.getId()+" 重复异常",new Exception());
		}else{
			providers.put(provider.getId(), provider);
		}
	}
	
	/**
	 * @param key
	 * @return
	 * @Description:获取Provider
	 */
	public Provider getProvider(String key){
		return providers.get(key);
	}
	
	/**
	 * @return
	 * @Description:获取Provider数量
	 */
	public int getProviderSize(){
		return providers.size();
	}
	
	public static MapperContainer getInstance(){
		if(instance==null){
			synchronized (MapperContainer.class) {
				if(instance==null){
					instance=new MapperContainer();
				}
			}
		}
		return instance;
	}
}

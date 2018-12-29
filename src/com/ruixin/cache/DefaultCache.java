package com.ruixin.cache;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

import com.ruixin.annotation.Bean;
import com.ruixin.config.BeanFactory;

/**
 * @Author:ruixin
 * @Date: 2018年12月25日 上午9:15:44
 * @Description:Cache容器
 */
@Bean(init={"init"})
public class DefaultCache {

	private CacheManager cacheManager;
	private final String DefaultCache="defaultCache";
	
	/**
	 * @Description:初始化默认缓存
	 * 加载类时自动加载这个方法
	 */
	public void init(){
		cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
	                	.withCache(DefaultCache,CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, Object.class,
	                	ResourcePoolsBuilder.heap(100)).build()).build(true);
		BeanFactory.instance.addBeanObj("cacheManager", cacheManager);
	}
	
	public Cache<String, Object> getDeaultCache(){
		return cacheManager.getCache(DefaultCache, String.class, Object.class);
	}
	
	public CacheManager getCacheManager(){
		return cacheManager;
	}
}

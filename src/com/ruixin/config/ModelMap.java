package com.ruixin.config;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author:ruixin
 * @Date: 2018年11月14日 下午1:02:32
 * @Description:后台向前台传值
 */
public class ModelMap {

	private HttpServletRequest request;
	private String view;
	
	public ModelMap(HttpServletRequest request) {
		this.request=request;
	}
	
	/**
	 * @param key
	 * @param value
	 * @Description:向前端传值
	 */
	public void addParam(String key,Object value){
		request.setAttribute(key, value);
	}
	
	/**
	 * @param name
	 * @Description:设置返回的视图
	 */
	public void setView(String name){
		this.view=name;
	}
	
	/**
	 * @return
	 * @Description:获取视图
	 */
	public String getView(){
		return this.view;
	}
}

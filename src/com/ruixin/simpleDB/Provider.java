package com.ruixin.simpleDB;

/**
 * @Author:ruixin
 * @Date: 2018年12月11日 下午5:59:34
 * @Description:动态查询类
 */
public class Provider {

	/**
	 * Mapper的id属性
	 * 如果是注解，则默认ID是类的简化名.方法名
	 * 如果是xml，则是xml里面的id
	 */
	private String id;
	private String method;
	private Class<?> type;
	private String queryType;
	private Class<?> returnType;
	private SelectKey selectKey;
	private boolean useGeneratedKeys=false;
	
	
	public Provider(String providerId) {
		this.id=providerId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public Class<?> getReturnType() {
		return returnType;
	}

	public void setReturnType(Class<?> returnType) {
		this.returnType = returnType;
	}

	public SelectKey getSelectKey() {
		return selectKey;
	}

	public void setSelectKey(SelectKey selectKey) {
		this.selectKey = selectKey;
	}

	public boolean isUseGeneratedKeys() {
		return useGeneratedKeys;
	}

	public void setUseGeneratedKeys(boolean useGeneratedKeys) {
		this.useGeneratedKeys = useGeneratedKeys;
	}
	
}

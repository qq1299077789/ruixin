package com.ruixin.simpleDB;

/**
 * @Author:ruixin
 * @Date: 2018年12月11日 下午5:16:11
 * @Description:基本SQL操作类
 */
public class Mapper {
	
	/**
	 * Mapper的id属性
	 * 如果是注解，则默认ID是类的简化名.方法名
	 * 如果是xml，则是xml里面的id
	 */
	private String id;
	
	private String sql;

	/*类型 insert delete update select*/
	private String type;
	
	/**
	 * 返回类型，select根据设置值返回，insert默认返回Integer
	 */
	private Class<?> returnType;

	private SelectKey selectKey;
	
	private boolean useGeneratedKeys=false; 
	
	public Mapper(String id,String type){
		this.id=id;
		this.type=type;
	}
	
	public Mapper(String id,String type,String sql){
		this.id=id;
		this.type=type;
		this.sql=sql;
	}
	
	public Mapper(String id,String type,String sql,Class<?> returnType){
		this.id=id;
		this.sql=sql;
		this.type=type;
		this.returnType=returnType;
	}
	
	public Mapper(String id,String type,String sql,Class<?> returnType,boolean useGeneratedKeys){
		this.id=id;
		this.sql=sql;
		this.type=type;
		this.returnType=returnType;
		this.useGeneratedKeys=useGeneratedKeys;
	}
	
	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Class<?> getReturnType() {
		return returnType;
	}

	public void setReturnType(Class<?> returnType) {
		this.returnType = returnType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

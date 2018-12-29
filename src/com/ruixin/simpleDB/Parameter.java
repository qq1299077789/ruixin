package com.ruixin.simpleDB;

/**
 * @Author:ruixin
 * @Date: 2018年12月12日 上午10:18:48
 * @Description:Param注解
 */
public class Parameter {

	// 所在类名
	private String className;
	// 方法名
	private String methodName;
	// 参数位置 从0开始
	private int index;
	// Param值
	private String name;
	// 参数类型
	private Class<?> type;

	public Parameter(String className,String methodName,int index,String name,Class<?> type) {
		this.className=className;
		this.methodName=methodName;
		this.index=index;
		this.name=name;
		this.type=type;
	}
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

}

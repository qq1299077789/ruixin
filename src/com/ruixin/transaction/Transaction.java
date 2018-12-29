package com.ruixin.transaction;

/**
 * @Author:ruixin
 * @Date: 2018年12月14日 上午10:00:29
 * @Description:注解实体
 */
public class Transaction {

	private String className;
	private String methodName;
	private boolean readOnly;
	private int level;

	public Transaction(String className,String methodName,boolean readOnly){
		this.className=className;
		this.methodName=methodName;
		this.readOnly=readOnly;
	}
	
	public Transaction(String className,String methodName,boolean readOnly,int level){
		this.className=className;
		this.methodName=methodName;
		this.readOnly=readOnly;
		this.level=level;
	}
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
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

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
}

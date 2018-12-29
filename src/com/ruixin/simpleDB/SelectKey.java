package com.ruixin.simpleDB;

import com.ruixin.annotation.Order;

/**
 * @Author:ruixin
 * @Date: 2018年12月27日 上午9:28:08
 * @Description:SelectKey查询
 */
public class SelectKey {

	private String statement;
	
	private Class<?> resultType;
	
	private Order order;
	
	private String keyProperty;

	public SelectKey(String statement,Class<?> resultType,Order order,String keyProperty){
		this.statement=statement;
		this.resultType=resultType;
		this.order=order;
		this.keyProperty=keyProperty;
	}
	
	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public Class<?> getResultType() {
		return resultType;
	}

	public void setResultType(Class<?> resultType) {
		this.resultType = resultType;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public String getKeyProperty() {
		return keyProperty;
	}

	public void setKeyProperty(String keyProperty) {
		this.keyProperty = keyProperty;
	}
	
	
}

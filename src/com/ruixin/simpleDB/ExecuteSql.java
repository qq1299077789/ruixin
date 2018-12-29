package com.ruixin.simpleDB;

/**
 * @Author:ruixin
 * @Date: 2018年12月12日 下午1:39:08
 * @Description:可执行的SQL
 */
public class ExecuteSql {

	//可执行的SQL(包含？占位符)
	private String sql;
	//参数
	private Object[] args;

	public ExecuteSql(String sql,Object[] args){
		this.sql=sql;
		this.args=args;
	}
	
	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

}

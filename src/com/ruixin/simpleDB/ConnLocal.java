package com.ruixin.simpleDB;

import java.sql.Connection;
/**
 * @Author:ruixin
 * @Date: 2018年12月14日 下午12:50:07
 * @Description:数据库Connection线程一致
 */
public class ConnLocal {

	private static final ThreadLocal<Connection> connLocal = new ThreadLocal<Connection>();
	
	/**
	 * @param connection
	 * @Description:添加Connection
	 */
	public static void setConnection(Connection connection){
		connLocal.set(connection);
	}
	
	/**
	 * @return
	 * @Description:获取Connection
	 */
	public static Connection getConnection(){
		return connLocal.get();
	}
	
	/**
	 * @Description:移除Connection
	 */
	public static void removeConnection(){
		connLocal.remove();
	}
}

package com.ruixin.simpleDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import com.ruixin.config.HandleException;
import com.ruixin.util.LoggerUtils;
import com.ruixin.util.PropertiesUtil;
/**
 * @Author:ruixin
 * @Date: 2018年11月30日 上午11:01:02
 * @Description:数据库管理工具
 */
public class DBManager{

	private LinkedList<Connection> connpools=new LinkedList<>();
	private static DBManager manager=null;
	private String driver;
	private String url;
	private String username;
	private String password;
	private int poolSize=20;
	
	private DBManager(){
		this.driver=PropertiesUtil.getProperty("db.dirver");
		this.url=PropertiesUtil.getProperty("db.url");
		this.username=PropertiesUtil.getProperty("db.name");
		this.password=PropertiesUtil.getProperty("db.pass");
		this.poolSize=Integer.parseInt(PropertiesUtil.getProperty("db.poolSize"));
	}
	
	public DBManager(String driver,String url,String username,String password,int poolSize){
		this.driver=driver;
		this.url=url;
		this.username=username;
		this.password=password;
		this.poolSize=poolSize;
	}
	
	/**
	 * @Description:初始化数据库连接池
	 */
	public void register(){
		LoggerUtils.info("初始化数据库连接池...");
		try {
			Class.forName(driver);
			for(int i=0;i<poolSize;i++){
				Connection connection=DriverManager.getConnection(url, username, password);
				connpools.add(connection);
			}
		} catch (Exception e) {
			HandleException.getInstance().handler("数据库连接池初始化失败",e);
		}
	}
	
	/**
	 * @Description:获取数据库连接
	 */
	public Connection getConnention(){
		if(poolSize<1){
			HandleException.getInstance().handler(new RuntimeException("数据库连接池太拥挤"));
		}
		Connection conn=connpools.removeFirst();
		return conn;
	}
	
	/**
	 * @Description:释放数据库连接
	 */
	public void release(Connection conn){
		connpools.add(conn);
	}
	
	/**
	 * @param connection
	 * @param statement
	 * @param resultSet
	 * @Description:释放数据库连接
	 */
	public void release(Connection connection,Statement statement,ResultSet resultSet){
		try {
			if(statement!=null){
				statement.close();
			}
			if(resultSet!=null){
				resultSet.close();
			}
		} catch (SQLException e) {
			HandleException.getInstance().handler(new RuntimeException("数据库连接释放异常"));
		}finally {
			connpools.add(connection);
		}
	}
	
	/**
	 * @param statement
	 * @Description:释放资源
	 */
	public void release(Statement statement,ResultSet resultSet){
		try {
			if(statement!=null){
				statement.close();
			}
			if(resultSet!=null){
				resultSet.close();
			}
		} catch (SQLException e) {
			HandleException.getInstance().handler(new RuntimeException("数据库连接释放异常"));
		}
	}
	
	/**
	 * @param statement
	 * @Description:释放资源
	 */
	public void release(Statement statement){
		try {
			if(statement!=null){
				statement.close();
			}
		} catch (SQLException e) {
			HandleException.getInstance().handler(new RuntimeException("数据库连接释放异常"));
		}
	}
	
	/**
	 * @param connection
	 * @param statement
	 * @Description:释放数据库连接
	 */
	public void release(Connection connection,Statement statement){
		try {
			if(statement!=null){
				statement.close();
			}
		} catch (SQLException e) {
			HandleException.getInstance().handler(new RuntimeException("数据库连接释放异常"));
		}finally {
			connpools.add(connection);
		}
	}
	
	/**
	 * @Description:数据库连接空闲数
	 */
	public int availableConnSize(){
		return connpools.size();
	}
	
	/**
	 * @return
	 * @Description:获取Manager对象
	 */
	public static DBManager getManager(){
		if(manager==null){
			synchronized(DBManager.class){
				if(manager==null){
					manager=new DBManager();
				}
			}
		}
		return manager;
	}
	
	/**
	 * @param connection
	 * @Description:事务提交
	 */
	public void commit(Connection connection){
		try {
			connection.commit();
		} catch (SQLException e) {
			HandleException.getInstance().handler("事务提交失败", e);
		}
	}
	
	/**
	 * @param connection
	 * @Description:事务回滚
	 */
	public void rollback(Connection connection){
		try {
			connection.rollback();
		} catch (SQLException e) {
			HandleException.getInstance().handler("事务回滚失败", e);
		}
	}
}

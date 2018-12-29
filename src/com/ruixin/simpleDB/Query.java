package com.ruixin.simpleDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import com.ruixin.config.HandleException;
import com.ruixin.util.LoggerUtils;

/**
 * @Author:ruixin
 * @Date: 2018年12月12日 下午2:45:01
 * @Description:数据库操作类
 */
public class Query {

	/**
	 * @param sql
	 * @param params
	 * @return
	 * @Description:更新操作
	 */
	public static int update(String sql,boolean useGeneratedKeys,Object...params){
		PreparedStatement preparedStatement=null;
		Connection connection=null;
		try{
			connection=ConnLocal.getConnection();
			preparedStatement=connection.prepareStatement(sql);
			for(int i=0;params!=null&&i<params.length;i++){
				preparedStatement.setObject(i+1, params[i]);
			}
			int result=preparedStatement.executeUpdate();
			if(useGeneratedKeys){
				ResultSet resultSet = preparedStatement.getGeneratedKeys();
				 if (resultSet.next())
					 result = resultSet.getInt(1);
			}
			return result;
		}catch(SQLException e){
			HandleException.getInstance().handler("update执行异常",e);
		}finally {
			new Query().sqlLog(sql,null,preparedStatement,params);
			DBManager.getManager().release(preparedStatement);
		}
		return 0;
	}
	
	/**
	 * @param sql
	 * @param handler
	 * @param params
	 * @return
	 * @Description:查询数据
	 */
	public static List<Object> query(String sql,Class<?> returnType, Object ... params){
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;
		Connection connection=null;
		try{
			connection=ConnLocal.getConnection();
			preparedStatement=connection.prepareStatement(sql);
			for(int i=0;params!=null&&i<params.length;i++){
				preparedStatement.setObject(i+1, params[i]);
			}
			resultSet=preparedStatement.executeQuery();
			return ResultCovert.cover(resultSet, returnType);
		}catch(Throwable e){
			HandleException.getInstance().handler("query查询异常", e);
		}finally {
			new Query().sqlLog(sql, resultSet, preparedStatement, params);
			DBManager.getManager().release(preparedStatement,resultSet);
		}
		return null;
	}
	
	/**
	 * @param resultSet
	 * @return
	 * @Description:获取查询条数
	 */
	private static int getCount(ResultSet resultSet){
		try {
			resultSet.last();
			return resultSet.getRow();
		} catch (SQLException e) {
			HandleException.getInstance().handler("获取查询条数异常", e);
		}
		return 0;
	}
	
	/**
	 * @param sql
	 * @param resultSet
	 * @param params
	 * @throws SQLException 
	 * @Description:打印执行的SQL
	 */
	private void sqlLog(String sql,ResultSet resultSet,PreparedStatement preparedStatement,Object ... params){
		try{
			LoggerUtils.info("SQL:"+sql);
			LoggerUtils.info("Statement:"+Arrays.toString(params));
			if(resultSet!=null){
				LoggerUtils.info("ResultCount:"+getCount(resultSet));			
			}else{
				LoggerUtils.info("UpdateCount:"+preparedStatement.getUpdateCount());
			}			
		}catch(Exception e){
			HandleException.getInstance().handler("SQL日志异常",e);
		}
	}
}

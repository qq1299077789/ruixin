package com.ruixin.proxy;

import java.lang.reflect.Method;
import java.sql.Connection;

import com.ruixin.config.HandleException;
import com.ruixin.simpleDB.ConnLocal;
import com.ruixin.simpleDB.DBManager;
import com.ruixin.transaction.Transaction;
import com.ruixin.transaction.TransactionFactory;

import net.sf.cglib.proxy.MethodProxy;
/**
 * @Author:ruixin
 * @Date: 2018年12月14日 上午9:21:51
 * @Description:Service层代理处理
 */
public class ServiceProxyHandler implements ProxyHandler{

	@Override
	public Object handler(Object obj,Method method,Object[] args,MethodProxy proxy) {
		String className=method.getDeclaringClass().getSimpleName();
		Transaction transaction = TransactionFactory.getInstance().getTransaction(className, method.getName());
		Connection connection=DBManager.getManager().getConnention();
		Object result=null;
		try{
			connection.setAutoCommit(false);
			if(transaction!=null){
				connection.setReadOnly(transaction.isReadOnly());
				connection.setTransactionIsolation(transaction.getLevel());
			}
			ConnLocal.setConnection(connection);
			result=proxy.invokeSuper(obj, args);
			DBManager.getManager().commit(connection);
		}catch(Throwable e){
			HandleException.getInstance().handler("调用Service错误", e);
			DBManager.getManager().rollback(connection);
		}finally {
			DBManager.getManager().release(connection);
			ConnLocal.removeConnection();
		}		
		return result;
	}

}

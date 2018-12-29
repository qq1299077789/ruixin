package com.ruixin.transaction;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author:ruixin
 * @Date: 2018年12月14日 上午9:59:32
 * @Description:事务管理
 */
public class TransactionFactory {

	/*service,method,Transaction*/
	private Map<String,Map<String,Transaction>> factory=new HashMap<>();
	private static TransactionFactory instance=null;
	private TransactionFactory(){}
	
	/**
	 * @param transaction
	 * @Description:增加transaction
	 */
	public void addTransaction(Transaction transaction){
		if(!factory.containsKey(transaction.getClassName())){
			factory.put(transaction.getClassName(), new HashMap<>());
		}
		factory.get(transaction.getClassName()).put(transaction.getMethodName(), transaction);
	}
	
	/**
	 * @param className
	 * @param methodName
	 * @return
	 * @Description:获取Transaction
	 */
	public Transaction getTransaction(String className,String methodName){
		if(!factory.containsKey(className)){
			return null;
		}
		return factory.get(className).get(methodName);
	}
	
	public static TransactionFactory getInstance(){
		if(instance==null){
			synchronized (TransactionFactory.class) {
				if(instance==null){
					instance=new TransactionFactory();
				}
			}
		}
		return instance;
	}
}

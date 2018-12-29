package com.ruixin.annotation;
/**
 * @Author:ruixin
 * @Date: 2018年12月14日 上午11:33:24
 * @Description:事务级别
 */
public interface TransactionLevel {

	int NONE = 0;
	int TREAD_UNCOMMITTED = 1;
	int READ_COMMITTED = 2;
	int REPEATABLE_READ = 4;
	int SERIALIZABLE = 8;
}

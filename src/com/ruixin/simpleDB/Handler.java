package com.ruixin.simpleDB;

import java.sql.ResultSet;
import java.util.List;
/**
 * @Author:ruixin
 * @Date: 2018年12月12日 下午2:58:50
 * @Description:类型转换
 */
public interface Handler {

	List<Object> listHandle(ResultSet resultSet);
	
	Object handle(ResultSet resultSet);
}

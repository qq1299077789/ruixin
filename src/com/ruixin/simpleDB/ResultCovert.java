package com.ruixin.simpleDB;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruixin.util.ReflectUtil;
import com.ruixin.util.TypeConvertor;

public class ResultCovert {

	/**
	 * @param resultSet
	 * @param returnType
	 * @throws Throwable
	 * @Description:结果转换
	 */
	public static List<Object> cover(ResultSet resultSet,Class<?> returnType) throws Throwable{
		if(returnType==Map.class){
			return coverToMap(resultSet);
		}else if(TypeConvertor.isGeneral(returnType)){
			return coverToGeneral(resultSet);
		}
		return covertToBean(resultSet,returnType);
	}
	
	/**
	 * @param resultSet
	 * @param resultType
	 * @throws Throwable
	 * @Description:转化成bean
	 */
	public static List<Object> covertToBean(ResultSet resultSet,Class<?> resultType) throws Throwable{
		List<Object> results=new ArrayList<>();
		while(resultSet.next()){
			Object object = resultType.newInstance();
			ResultSetMetaData metaData = resultSet.getMetaData();
			for(int i=0;i<metaData.getColumnCount();i++){
				String columnKey=metaData.getColumnLabel(i);
				Object value=resultSet.getObject(i);
				if(value!=null){
					String[] objs=columnKey.split("\\.");
					Object object1=object;
					for(String obj:objs){
						Map<String,Field> fieldMap=ReflectUtil.instance.getMapFields(object1.getClass());
						if(fieldMap.containsKey(obj.trim())){
							Field field=fieldMap.get(obj.trim());
							if(field.get(object1)==null){
								field.set(object1, field.getType().newInstance());							
							}
							object1=field.get(object1);
						}else{
							break;
						}
					}
					if(object1!=null){
						String column1=objs[objs.length-1].trim();
						Map<String, Field> mapFields = ReflectUtil.instance.getMapFields(object1.getClass());
						if(mapFields.containsKey(column1)){
							Field field=mapFields.get(column1);
							field.set(object1,value);
						}
					}
				}
			}
		}
		return results;
	}
	
	/**
	 * @param resultSet
	 * @return
	 * @throws Throwable
	 * @Description:转换成Map
	 */
	public static List<Object> coverToMap(ResultSet resultSet) throws Throwable{
		List<Object> results=new ArrayList<Object>();
		while(resultSet.next()){
			Map<String,Object> result=new HashMap<>();
			ResultSetMetaData metaData = resultSet.getMetaData();
			for(int i=0;i<metaData.getColumnCount();i++){
				String columnKey=metaData.getColumnLabel(i);
				Object value=resultSet.getObject(i);
				result.put(columnKey, value);
			}
			results.add(result);
		}
		return null;
	}
	
	/**
	 * @param resultSet
	 * @throws Throwable
	 * @Description:转化为常用类
	 */
	public static List<Object> coverToGeneral(ResultSet resultSet) throws Throwable{
		List<Object> results=new ArrayList<Object>();
		while(resultSet.next()){
			results.add(resultSet.getObject(1));
		}
		return results;
	}
}

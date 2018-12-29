package com.ruixin.simpleDB;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ruixin.config.HandleException;
import com.ruixin.util.ReflectUtil;
import com.ruixin.util.StringUtil;

/**
 * @Author:ruixin
 * @Date: 2018年12月12日 上午11:33:06
 * @Description:sql语句格式化
 */
public class SQLFormatter {

	private String sql;
	private List<Parameter> params;
	private Object[] args;
	
	public SQLFormatter(String sql,List<Parameter> params,Object[] args) {
		this.sql=sql;
		this.params=params;
		this.args=args;
	}
	
	/**
	 * 占位符#{}
	 * @Description:将包含占位符的SQL语句格式化
	 */
	public ExecuteSql format(){
		if(StringUtil.isBlank(sql)){
			HandleException.getInstance().handler("SQL语句不能为空", new Exception());
		}
		//获取#{}里面的值，然后转化成对应的参数
		Pattern pattern=Pattern.compile("(?<=\\#\\{)[^\\}]+(?=\\})");
		Matcher matcher = pattern.matcher(sql);
		List<Object> execArgs=new ArrayList<>();
		while(matcher.find()){
			String para=matcher.group();
			Object arg = getPara(para);
			execArgs.add(arg);
		}
		//将占位符#{} 转化为 ?
		Pattern pattern1=Pattern.compile("\\#\\{([a-zA-Z0-9\\[\\]\\.]+)\\}");
		Matcher matcher1 = pattern1.matcher(sql);
		String execSql=sql;
		while(matcher1.find()){
			execSql=execSql.replace(matcher1.group(), "?");
		}
 		return new ExecuteSql(execSql,execArgs.toArray());
	}
	
	/**
	 * @param para
	 * @return
	 * @Description:通过Sql里面的占位符获取对象的参数
	 */
	private Object getPara(String para){
		//对象.值 实体对象，Map
		if(para.contains(".")&&!para.contains("[")){
			String[] strs=para.split("\\.");
			Parameter parameter = getParameter(strs[0]);
			Object arg = args[parameter.getIndex()];
			for(int i=1;i<strs.length;i++){
				String fieldName=strs[i];
				arg=getData(arg,fieldName);
			}
			return arg;
		}else if(!para.contains(".")&&para.contains("[")){
			//数组、list
			
		}else if(para.contains(".")&&para.contains("[")){
			//数组、list 对象.属性
			
		}else{
			Parameter parameter = getParameter(para);
			Object arg=args[parameter.getIndex()];
			return arg;
		}
		return null;
	}
	
	/**
	 * @param para
	 * @return
	 * @Description:通过Sql里面的占位符获取Param注解
	 */
	private Parameter getParameter(String para){
		Parameter parameter=null;
		for(Parameter param:params){
			if(param.getName().equals(para)){
				parameter=param;
				break;
			}
		}
		if(parameter==null){
			HandleException.getInstance().handler(para+"获取不到Param", new Exception());
		}
		return parameter;
	}
	
	/**
	 * @param object
	 * @param field
	 * @return
	 * @Description:获取对象.属性的值
	 */
	@SuppressWarnings("rawtypes")
	private Object getData(Object object,String field){
		if(object==null){
			return null;
		}
		if(object.getClass()==Map.class){
			return ((Map)object).get(field);
		}else{
			Field field2 = ReflectUtil.instance.getField(object.getClass(), field);
			try{
				return field2.get(object);				
			}catch(Exception e){
				HandleException.getInstance().handler(object.getClass().getSimpleName()+"."+field+"获取失败", e);
			}
		}
		return null;
	}
	
}

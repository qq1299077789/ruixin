package com.ruixin.simpleDB;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import com.ruixin.annotation.Order;
import com.ruixin.config.HandleException;
import com.ruixin.page.Page;
import com.ruixin.page.PageHelper;
import com.ruixin.util.ReflectUtil;
import com.ruixin.util.StringUtil;
/**
 * @Author:ruixin
 * @Date: 2018年12月12日 上午11:25:06
 * @Description:Mapper，Provider处理类
 */
public class MapperHandler {

	private List<Parameter> params;
	private Method method;
	private Object[] args;
	
	public MapperHandler(Method method, Object[] args, List<Parameter> params) {
		this.method=method;
		this.args=args;
		this.params=params;
	}

	/**
	 * @param mapper 
	 * @return
	 * @Description:处理Mapper
	 */
	public Object handleMapper(Mapper mapper){
		SelectKey selectKey = mapper.getSelectKey();
		if(selectKey==null){
			return execSql(mapper.getSql(),mapper.getType(),mapper.getReturnType(),mapper.isUseGeneratedKeys());			
		}else{
			Object result=null;
			if(selectKey.getOrder().equals(Order.BEFORE)){
				handleSelectKey(selectKey);
			}
			result=execSql(mapper.getSql(),mapper.getType(),mapper.getReturnType(),mapper.isUseGeneratedKeys());
			if(selectKey.getOrder().equals(Order.AFTER)){
				handleSelectKey(selectKey);
			}
			return result;
		}
	}
	
	/**
	 * @param providor
	 * @return
	 * @Description:处理Providor
	 */
	public Object handleProvider(Provider provider){
		String methodName = provider.getMethod();
		Class<?> type = provider.getType();
		Method providerMethod=ReflectUtil.instance.getMethod(type,methodName);
		Object providerObj=ReflectUtil.instance.getInstance(type);
		try {
			Object sql = ReflectUtil.instance.callMethod(providerObj, providerMethod, args);
			Object result=null;
			SelectKey selectKey=provider.getSelectKey();
			if(selectKey.getOrder().equals(Order.BEFORE)){
				handleSelectKey(selectKey);
			}
			result=execSql(StringUtil.toString(sql),provider.getQueryType(),provider.getReturnType(),provider.isUseGeneratedKeys());
			if(selectKey.getOrder().equals(Order.AFTER)){
				handleSelectKey(selectKey);
			}
			return result;
		} catch (Exception e) {
			HandleException.getInstance().handler(method.getName()+" Provider调用失败", e);
		}
		return null;
	}
	
	/**
	 * @param selectKey
	 * @Description:处理SelectKey
	 */
	public void handleSelectKey(SelectKey selectKey){
		Object result=execSql(selectKey.getStatement(), QueryType.SELECT, selectKey.getResultType(),false);
		if(args.length==1&&result!=null){
			Field field = ReflectUtil.instance.getField(args[0].getClass(), selectKey.getKeyProperty());
			ReflectUtil.instance.invokField(args[0], field, result, false);
		}
	}
	
	/**
	 * @param sql
	 * @param type
	 * @return
	 * @Description:执行sql语句
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object execSql(String sql,String type,Class<?> returnType,boolean useGeneratedKeys){
		SQLFormatter sqlFormatter=new SQLFormatter(sql, params, args);
		ExecuteSql executeSql = sqlFormatter.format();
		Object result=null;
		if(type.equals(QueryType.SELECT)){
			sql = executeSql.getSql();
			Page page = PageHelper.getPage();
			if(page!=null){
				sql=sql+" LIMIT "+page.getPageNumer()+" , "+page.getPageSize();
			}
			result=Query.query(sql,returnType, executeSql.getArgs());
			if(method.getReturnType()!=List.class&&result!=null){
				List<Object> list=(List<Object>)result;
				if(list.size()==1){
					result=list.get(0);
					if(page!=null){
						page.setPageCount(1);						
					}
				}else if(list.size()>1){
					HandleException.getInstance().handler("查询出多条数据异常", new Exception());
				}else{
					result=null;
				}
			}
			if(page!=null){
				page.setPageCount(((List)result).size());				
			}
		}else{
			result=Query.update(executeSql.getSql(),useGeneratedKeys,executeSql.getArgs());
		}
		return result;
	}
}

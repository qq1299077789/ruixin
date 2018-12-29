package com.ruixin.util;
/**
 * @Author:ruixin
 * @Date: 2018年12月12日 下午1:12:49
 * @Description:类型转换工具类
 */
public class TypeConvertor {

	/**
	 * @param clz
	 * @param arg
	 * @return
	 * @Description:通过对象转类型
	 */
	public static Object parseData(Class<?> clz,Object arg){
		boolean isNull=arg==null;
		if(clz==String.class){
			return isNull?arg:arg.toString();
		}else if(clz==int.class||clz==Integer.class){
			return isNull?0:Integer.parseInt(arg.toString());
		}else if(clz==float.class||clz==Float.class){
			return isNull?0f:Float.parseFloat(arg.toString());
		}else if(clz==double.class||clz==Double.class){
			return isNull?0d:Double.parseDouble(arg.toString());
		}else if(clz==boolean.class||clz==Boolean.class){
			return isNull?false:Boolean.parseBoolean(arg.toString());
		}else if(clz==long.class||clz==Long.class){
			return isNull?0L:Long.parseLong(arg.toString());
		}else if(clz==char.class||clz==Character.class){
			return isNull?'\u0000':arg.toString().charAt(0);
		}else if(clz==short.class||clz==Short.class){
			return isNull?0:Short.parseShort(arg.toString());
		}else{
			return arg;
		}
	}
	
	public static boolean isGeneral(Class<?> clz){
		if(clz==String.class){
			return true;
		}else if(clz==int.class||clz==Integer.class){
			return true;
		}else if(clz==float.class||clz==Float.class){
			return true;
		}else if(clz==double.class||clz==Double.class){
			return true;
		}else if(clz==boolean.class||clz==Boolean.class){
			return true;
		}else if(clz==long.class||clz==Long.class){
			return true;
		}else if(clz==char.class||clz==Character.class){
			return true;
		}else if(clz==short.class||clz==Short.class){
			return true;
		}else{
			return false;
		}
	}
}

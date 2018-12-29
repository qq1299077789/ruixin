package com.ruixin.util;
/**
 * @Author:ruixin
 * @Date: 2018年12月11日 下午1:01:32
 * @Description:String工具类
 */
public class StringUtil {

	public static boolean isBlank(String str){
		if(str==null||str.length()==0){
			return true;
		}
		return false;
	}
	
	public static boolean isNotBlank(String str){
		return !isBlank(str);
	}
	
	public static String toString(Object object){
		if(object==null){
			return null;
		}
		return String.valueOf(object);
	}
}

package com.ruixin.util;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * @Author:ruixin
 * @Date: 2018年8月30日 上午11:03:02
 * @Description:日志工具类
 */
public class LoggerUtils {

	public static void info(Object message,Throwable throwable){
		System.out.println(logFormat(message));
		throwable.printStackTrace();
	}
	
	public static void info(Object message){
		System.out.println(logFormat(message));
	}
	
	public static void debug(Object message,Throwable throwable){
		System.err.println(logFormat(message));
		throwable.printStackTrace();
	}
	
	public static void debug(Object message){
		System.err.println(logFormat(message));
	}
	
	private static String logFormat(Object message){
		StringBuffer buffer=new StringBuffer();
		  StackTraceElement[] stacks = new Throwable().getStackTrace();
		buffer.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date())).append("[").append(stacks[2].getClassName()+".").append(stacks[2].getMethodName()+"  ").append(stacks[2].getLineNumber()).append("] ==> ").append(message);
		return buffer.toString();
	}
}

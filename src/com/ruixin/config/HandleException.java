package com.ruixin.config;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author:ruixin
 * @Date: 2018年12月5日 下午9:03:02
 * @Description:异常处理类
 */
public class HandleException implements ExceptionType{

	private static HandleException instance=null;
	
	private HandleException(){
		
	}
	
	public void handler(Exception e){
		handler(null,e);
	}
	
	public void handler(String message,Throwable e){
		System.err.println(message);
		e.printStackTrace();
	}
	
	/**
	 * @param resp
	 * @param message
	 * @Description:处理视图上的问题
	 */
	public void handlerView(HttpServletResponse resp,String message){
		PrintWriter out = null;
		try {
			out = resp.getWriter();
			out.append(message);
		} catch (IOException e) {
			HandleException.getInstance().handler(e);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
	
	public static HandleException getInstance(){
		if(instance==null){
			synchronized (HandleException.class) {
				if(instance==null){
					instance=new HandleException();
				}
			}
		}
		return instance;
	}
	
}

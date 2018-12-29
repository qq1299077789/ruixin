package com.ruixin.listener;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author:ruixin
 * @Date: 2018年11月13日 下午2:29:56
 * @Description:Handler监听类
 */
public interface HandlerListener {

	/**
	 * @param request
	 * @param httpServletRequest
	 * @Description:处理handler之前执行
	 */
	public void before(HttpServletRequest request,HttpServletResponse resp);
	
	/**
	 * @param request
	 * @param httpServletRequest
	 * @Description:处理handler之后执行
	 */
	public void after(HttpServletRequest request,HttpServletResponse resp);
	
	
}

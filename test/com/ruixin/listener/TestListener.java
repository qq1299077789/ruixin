package com.ruixin.listener;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ruixin.util.LoggerUtils;

public class TestListener implements HandlerListener {

	@Override
	public void before(HttpServletRequest request, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		LoggerUtils.debug("Handler 监听测试 before1");
	}

	@Override
	public void after(HttpServletRequest request, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		LoggerUtils.debug("Handler 监听测试 after1");
	}

}

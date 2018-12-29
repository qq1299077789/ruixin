package com.ruixin.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ruixin.config.HandleException;
import com.ruixin.config.HandlerAdaptor;
import com.ruixin.config.ModelMap;
import com.ruixin.config.ViewAdaptor;
import com.ruixin.invok.HandlerListenerInvok;

/**
 * @Author:ruixin
 * @Date: 2018年12月5日 下午1:09:30
 * @Description:请求转发
 */
public class RequestDispatch {

	private HttpServletRequest req;
	private HttpServletResponse resp;

	public RequestDispatch(HttpServletRequest request, HttpServletResponse response) {
		this.req = request;
		this.resp = response;
	}

	/**
	 * @Description:请求转发
	 */
	public void dispatch() {
		HandlerAdaptor handler = new HandlerAdaptor(req, resp);
		HandlerListenerInvok.getInstance().doBefore(req, resp);
		Object result = handler.handle();
		HandlerListenerInvok.getInstance().doAfter(req, resp);
		if (result != null) {
			// 处理JSON返回
			if (handler.returnJson()) {
				PrintWriter out = null;
				try {
					resp.setContentType("application/json;charset=utf-8");
					out = resp.getWriter();
					out.append(result.toString());
				} catch (IOException e) {
					HandleException.getInstance().handler(e);
				} finally {
					if (out != null) {
						out.close();
					}
				}
			} else if (handler.returnModelMap()) {
				ViewAdaptor view = new ViewAdaptor(req, resp);
				ModelMap modelMap = (ModelMap) result;
				view.handleView(modelMap.getView());
			} else {
				ViewAdaptor view = new ViewAdaptor(req, resp);
				view.handleView(result);
			}
		}
	}

}

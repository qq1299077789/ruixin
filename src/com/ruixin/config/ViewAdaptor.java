package com.ruixin.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ruixin.util.PropertiesUtil;
import com.ruixin.util.StringUtil;
import com.ruixin.util.UrlUtil;

/**
 * @Author:ruixin
 * @Date: 2018年11月13日 下午2:11:30
 * @Description:视图层转化
 */
public class ViewAdaptor {

	private HttpServletRequest req;
	private HttpServletResponse resp;
	private String prePath="/WEB-INF/jsp/";//视图前缀
	private String sufPath=".jsp";//视图后缀
	
	public ViewAdaptor(HttpServletRequest req, HttpServletResponse resp) {
		this.req=req;
		this.resp=resp;
		init();
	}

	/**
	 * @Description:参数初始化
	 */
	private void init() {
		if(PropertiesUtil.getProperties().contains("web.prefix")){
			prePath=PropertiesUtil.getProperty("web.prefix");
		}
		if(PropertiesUtil.getProperties().contains("web.suffix")){
			sufPath=PropertiesUtil.getProperty("web.suffix");
		}
	}

	/**
	 * @param obj
	 * @Description:处理视图
	 */
	public void handleView(Object obj){
		if(obj==null||StringUtil.isBlank(obj.toString())){
			HandleException.getInstance().handlerView(this.resp,HandleException.NOTFOUND);
		}else{
			prePath=UrlUtil.formatUrl(prePath);
			String path=obj.toString();
			if(path.startsWith("forward:")){
				path=path.replace("forward:", "");
			}else if(path.startsWith("redirect:")){
				path=path.replace("redirect:", "");
				redirect(path);
				return;
			}
			String viewPath=prePath+UrlUtil.formatUrl(path)+sufPath;
			forward(viewPath);
		}
	}
	
	/**
	 * @param viewPath
	 * @Description:处理forward跳转
	 */
	private void forward(String viewPath){
		try {
			req.getRequestDispatcher(viewPath).forward(req, resp);
		} catch (ServletException | IOException e) {
			HandleException.getInstance().handlerView(this.resp,HandleException.NOTFOUND);
		}
	}
	
	/**
	 * @param viewPath
	 * @throws IOException 
	 * @Description:处理Redirect跳转
	 */
	private void redirect(String viewPath){
		try {
			resp.sendRedirect(viewPath);
		} catch (IOException e) {
			HandleException.getInstance().handlerView(this.resp,HandleException.ERROR);
		}
	}
}

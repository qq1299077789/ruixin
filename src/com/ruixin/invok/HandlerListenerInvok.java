package com.ruixin.invok;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ruixin.listener.HandlerListener;
import com.ruixin.util.ReflectUtil;
/**
 * @Author:ruixin
 * @Date: 2018年12月3日 下午3:09:17
 * @Description:handler执行监听
 */
public class HandlerListenerInvok {

	private List<Class<?>> subClasses=new ArrayList<Class<?>>();
	
	private static HandlerListenerInvok instance=null;
	
	private HandlerListenerInvok(){
		subClasses=ReflectUtil.instance.getSubclasses(HandlerListener.class);
	}
	
	/**
	 * @param req
	 * @param resp
	 * @Description:执行前调用
	 */
	public void doBefore(HttpServletRequest req,HttpServletResponse resp){
		subClasses.forEach(clz->{
			HandlerListener newInstance = (HandlerListener) ReflectUtil.instance.getInstance(clz);
			newInstance.before(req, resp);
		});
	}
	
	/**
	 * @param req
	 * @param resp
	 * @Description:执行后调用
	 */
	public void doAfter(HttpServletRequest req,HttpServletResponse resp){
		subClasses.forEach(clz->{
			HandlerListener newInstance = (HandlerListener) ReflectUtil.instance.getInstance(clz);
			newInstance.after(req, resp);
		});
	}
	
	/**
	 * @return
	 * @Description:获取单例
	 */
	public static HandlerListenerInvok getInstance(){
		if(instance==null){
			synchronized (HandlerListenerInvok.class) {
				if(instance==null){
					instance=new HandlerListenerInvok();
				}
			}
		}
		return instance;
	}
}

package com.ruixin.page;
/**
 * @Author:ruixin
 * @Date: 2018年12月14日 下午3:11:28
 * @Description:分页工具类
 */
public class PageHelper {

	private static final ThreadLocal<Page> LOCAL=new ThreadLocal<Page>();
	
	/**
	 * @param pageNum
	 * @param pageSize
	 * @Description:分页
	 */
	public static void limit(int pageNum,int pageSize){
		Page page = new Page(pageNum,pageSize);
		LOCAL.set(page);
	}
	
	/**
	 * @param pageSize
	 * @Description:
	 */
	public static void limit(int pageSize){
		Page page = new Page(pageSize);
		LOCAL.set(page);
	}
	
	/**
	 * @param page
	 * @Description:分页
	 */
	public static void setPage(Page page){
		LOCAL.set(page);
	}
	
	/**
	 * @return
	 * @Description:获取Page对象
	 */
	public static Page getPage(){
		return LOCAL.get();
	}
	
	
	/**
	 * @return
	 * @Description:获取每页条数
	 */
	public static int getPageSize(){
		if(LOCAL.get()==null){
			return 0;
		}
		return LOCAL.get().getPageSize();
	}
	
	/**
	 * @return
	 * @Description:获取当前页条数
	 */
	public static int getPageCount(){
		if(LOCAL.get()==null){
			return 0;
		}
		return LOCAL.get().getPageCount();
	}
	
	/**
	 * @Description:删除ThreadLocal中的Page对象
	 */
	public static void removePage(){
		LOCAL.remove();
	}
}

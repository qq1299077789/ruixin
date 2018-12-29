package com.ruixin.page;

/**
 * @Author:ruixin
 * @Date: 2018年12月14日 下午2:57:21
 * @Description:分页
 */
public class Page {

	/* 当前页 页数从0开始*/
	private int pageNumer = 0;
	/* 每页条数 */
	private int pageSize = 10;
	/* 当前页条数 */
	private int pageCount;

	public Page(int pageSize){
		this.pageSize=pageSize;
	}
	
	public Page(int pageNum, int pageSize) {
		this.pageNumer=pageNum;
		this.pageSize=pageSize;
	}

	public int getPageNumer() {
		return pageNumer;
	}

	public void setPageNumer(int pageNumer) {
		this.pageNumer = pageNumer;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

}

package com.beacon.wechat.app.comm;

import java.util.ArrayList;
import java.util.List;

public class PageResult<T> extends Result<List<T>>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -423678494476787139L;
	private int pageNo=1;
    private int pageSize=10;
    private long total=0;
    private List<T> data = new ArrayList<T>();
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	@Override
	public List<T> getData() {
		return this.data;
	}
	@Override
	public void setData(List<T> data) {
		this.data=data;
	}
}

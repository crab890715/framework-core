package com.framework.core.app.comm;

import java.io.Serializable;

public class Result<T> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4389582745444177494L;
	public final static int CODE_FAIL = -1;
	public final static int CODE_SUCCUSS = 200;
	public Result() {
		this.timetemp = System.currentTimeMillis();
		this.code = CODE_FAIL;
	}
	public Result(int code) {
		this();
		this.code = code;
	}
	public Result(int code, String msg) {
		this(code);
		this.msg = msg;
	}
	public Result(int code, String msg, T data) {
		this(code,msg);
		this.setData(data);
	}
	
	//返回状态码
	private int code;
	//返回消息
	private String msg;
	//时间戳
	private long timetemp;
	//返回数据
	private T data;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	/**
	 * @return the data
	 */
	public T getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(T data) {
		this.data = data;
	}
	/**
	 * @return the timetemp
	 */
	public long getTimetemp() {
		return timetemp;
	}
	/**
	 * @param timetemp the timetemp to set
	 */
	public void setTimetemp(long timetemp) {
		this.timetemp = timetemp;
	}
}

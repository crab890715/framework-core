package com.beacon.wechat.app.vo;

public class SignInfoVO extends BaseInfoVO{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5250841714573084251L;
	//手机
	private String mobile;
	//真实姓名
	private String realname;
	//邮箱
	private String email;
	//工号
	private String employeeNo;
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmployeeNo() {
		return employeeNo;
	}
	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}
	
}

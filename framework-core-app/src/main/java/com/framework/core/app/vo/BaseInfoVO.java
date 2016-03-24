package com.framework.core.app.vo;

import java.io.Serializable;

public class BaseInfoVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 590402824987891961L;
	private String openId;
	private String storeId;
	private String deviceId;
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
}

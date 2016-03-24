package com.framework.core.app.comm.runnable;

public class RedpacketsRunnable implements Runnable{

	private String openId;
	private String redId;
	private String serialNumber;
//	private RedpacketsBiz redpacketsBiz;
	public RedpacketsRunnable(String openId, String redId,String serialNumber) {
		super();
		this.openId = openId;
		this.redId = redId;
		this.serialNumber=serialNumber;
//		redpacketsBiz = AppUtils.spring(RedpacketsBiz.class);
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getRedId() {
		return redId;
	}
	public void setRedId(String redId) {
		this.redId = redId;
	}
	@Override
	public void run() {
//		redpacketsBiz.consumRedpacket(redId, openId,serialNumber);
	}
	/**
	 * @return the serialNumber
	 */
	public String getSerialNumber() {
		return serialNumber;
	}
	/**
	 * @param serialNumber the serialNumber to set
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

}

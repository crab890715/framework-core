package com.framework.core.app.vo;

public class OrderVO extends BaseInfoVO{

	private static final long serialVersionUID = -1607266387767563500L;

    private String couponName;

    private String orderOut;

	/**
	 * @return the orderOut
	 */
	public String getOrderOut() {
		return orderOut;
	}

	/**
	 * @param orderOut the orderOut to set
	 */
	public void setOrderOut(String orderOut) {
		this.orderOut = orderOut;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

}

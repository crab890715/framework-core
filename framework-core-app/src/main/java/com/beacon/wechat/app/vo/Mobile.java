package com.beacon.wechat.app.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Mobile implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7077917251705607578L;
	//{"Mobile":"13510278755","QueryResult":"True","TO":"中国移动","Corp":"中国移动","Province":"广东",
	//"City":"深圳","AreaCode":"0755","PostCode":"518000","VNO":"","Card":""}
	@JsonProperty(value = "Mobile")
	private String mobile;
	@JsonProperty(value = "QueryResult")
	private String queryResult;
	@JsonProperty(value = "TO")
	private String to;
	@JsonProperty(value = "Corp")
	private String corp;
	@JsonProperty(value = "Province")
	private String province;
	@JsonProperty(value = "City")
	private String city;
	@JsonProperty(value = "AreaCode")
	private String areaCode;
	@JsonProperty(value = "PostCode")
	private String postCode;
	@JsonProperty(value = "VNO")
	private String vno;
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getQueryResult() {
		return queryResult;
	}
	public void setQueryResult(String queryResult) {
		this.queryResult = queryResult;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getCorp() {
		return corp;
	}
	public void setCorp(String corp) {
		this.corp = corp;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public String getVno() {
		return vno;
	}
	public void setVno(String vno) {
		this.vno = vno;
	}
	public String getCard() {
		return card;
	}
	public void setCard(String card) {
		this.card = card;
	}
	@JsonProperty(value = "Card")
	private String card;
	
}

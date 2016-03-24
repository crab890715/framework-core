package com.beacon.wechat.app.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Dispatcher implements Serializable{
	public enum TYPE{
		NORMAL(1,"普通分发"),
		ACCOSS(2,"交叉分发"),
		ADS(3,"广告分发"),
		ERR(-1,"错误页面，什么也没摇到"),
		CUSTOM_NORMAL(4,"普通分发自定义URL"),
		CUSTOM_ACCOSS(5,"交叉分发自定义URL");
		TYPE(int value,String text){
			this.value=value;
			this.text=text;
		}
		private int value;
		private String text;
		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

		/**
		 * @return the text
		 */
		public String getText() {
			return text;
		}

		/**
		 * @param text the text to set
		 */
		public void setText(String text) {
			this.text = text;
		}
	}

	private static final Map<String, Integer> HISTORY_TEMP = new HashMap<String, Integer>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = -5603396911399531282L;

	{
		put("coupon",1);
		put("gold",2);
		put("flows",3);
		put("redpackets",4);
		put("wishwall",5);
		put("vote",6);
		put("crazyshake",7);
		put("custom",8);
		put("sign",9);
		put("shakeredpackets",10);
		put("payment",11);
		put("ad",12);
	}};
	/**
	 * 
	 */
	private static final long serialVersionUID = 3387109759182000202L;
	//普通分发
	private static final String[] normals = new String[]{
			"coupon","gold","flows","redpackets",
			"wishwall","vote","crazyshake","sign",
			"shakeredpackets","payment","ad"};
	//交叉分发
	private static final String[] accoss = new String[]{
			"coupon","gold","flows","redpackets",
			"wishwall","vote","crazyshake","sign",
			"shakeredpackets","payment","ad"};
	//广告分发
	private static final String[] ads = new String[]{
			"coupon","gold","flows","redpackets","custom"};
	//错误分发
	private static final String[] err = new String[]{
			"other"};

	public String page() {
		String result = "";
		String ext = ".shake";
		if(type>0){
			result = _historyTemp(this.type, this.page) + ext;
		}else{
			result = err[page-1];
		}
		return result;
	}
	public static Integer template(String temp){
		return HISTORY_TEMP.get(temp);
	}
	public static String historyTemp(int type,String temp){
		String index = "NM_";
		switch (type) {
		case 1:
		case 4://普通分发自定义
			index="NM_";
			break;
		case 2:
		case 5://交叉分发自定义
			index="JX_";
			break;
		case 3:
			index="AD_";
			break;
		}
		Integer t = template(temp);
		return index + (t==null ? template("custom") : t);
	}
	public static String historyTemp(int type,int temp){
		String result = _historyTemp(type, temp);
		return historyTemp(type,result);
	}
	private static String _historyTemp(int type,int temp){
		String result = "";
		switch (type) {
		case 1:
			result = normals[temp-1];
			break;
		case 2:
			result = accoss[temp-1];
			break;
		case 3:
			result = ads[temp-1];
			break;
		case 4://普通分发自定义
		case 5://交叉分发自定义
			result = "custom";
			break;
		}
		return result;
	}
	
	private int page;
	private int type;
	private int pagestore;
	public Dispatcher() {
		super();
		this.page = 1;
	}
	public Dispatcher(int page, int type) {
		super();
		this.page = page;
		this.type = type;
	}
	public Dispatcher(int page, int type, int pagestore) {
		super();
		this.page = page;
		this.type = type;
		this.pagestore = pagestore;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * @return the pagestore
	 */
	public int getPagestore() {
		return pagestore;
	}
	/**
	 * @param pagestore the pagestore to set
	 */
	public void setPagestore(int pagestore) {
		this.pagestore = pagestore;
	}
}

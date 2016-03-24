package com.beacon.wechat.app.comm.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;

import com.beacon.wechat.app.biz.WeixinBiz;
import com.beacon.wechat.app.comm.utils.AppUtils;

public class WeixinTag extends TagSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8997239144745198831L;
	private final String DEFAULT = "jssdk";
	private String name;
	@Override
	public int doEndTag() throws JspException {
		if(StringUtils.isBlank(name)){
			name = DEFAULT;
		}
//		WeixinBiz weixinBiz = WebApplicationContextUtils
//				.getRequiredWebApplicationContext(this.pageContext.getServletContext())
//				.getBean(WeixinBiz.class);
		WeixinBiz weixinBiz = AppUtils.spring(WeixinBiz.class);
		this.pageContext.setAttribute(name, weixinBiz.jssdk());
		return super.doEndTag();
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
